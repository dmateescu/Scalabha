package opennlp.scalabha.tag.hmm

import opennlp.scalabha.tag.support.CondCountsTransformer
import opennlp.scalabha.tag.support.DefaultedCondFreqCounts
import opennlp.scalabha.tag.support.DefaultedFreqCounts
import opennlp.scalabha.tag.support.PassthroughCondCountsTransformer

class TransitionCountsTransformer[Tag](delegate: CondCountsTransformer[Option[Tag], Option[Tag]])
  extends CondCountsTransformer[Option[Tag], Option[Tag]] {

  override def apply(counts: DefaultedCondFreqCounts[Option[Tag], Option[Tag], Double]) = {
    DefaultedCondFreqCounts(
      delegate(counts).counts.map {
        case (tag, dfc @ DefaultedFreqCounts(c, t, d)) =>
          tag -> (tag match {
            case None => DefaultedFreqCounts(c + (None -> 0.), t, d)
            case _ => dfc
          })
      })
  }

}

object TransitionCountsTransformer {
  def apply[Tag]() = {
    new TransitionCountsTransformer(PassthroughCondCountsTransformer[Option[Tag], Option[Tag]]())
  }
}

package edu.arizona.sista.odin.toydomain

import edu.arizona.sista.struct.Interval
import edu.arizona.sista.processors.Document
import edu.arizona.sista.processors.bionlp.BioNLPProcessor
import edu.arizona.sista.odin._

object TestMatcher extends App {
  // two example sentences with manually defined named entity tags in IOB notation
  val sentence0 = "TGFBR2 phosphorylates peri-kappa B and inhibits the ubiquitination of SMAD3."
  val entities0 = Array("B-Protein", "O", "B-Protein", "I-Protein", "O", "O", "O", "O", "O", "B-Protein", "O")
  val sentence1 = "TGFBR2 binds to TGFBR1 and SMAD3."
  val entities1 = Array("B-Protein", "O", "O", "B-Protein", "O", "B-Protein", "O")

  // concatenate sentences
  val text = s"$sentence0 $sentence1"

  // read rules from rules.yml file in resources
  val stream = getClass.getResourceAsStream("/rules.yml")
  val rules = io.Source.fromInputStream(stream).mkString

  // creates an extractor engine using the rules and the default actions
  val extractor = new ExtractorEngine(rules)

  // annotate the sentences and override the named entity tags
  val proc = new BioNLPProcessor
  val doc = proc annotate text
  doc.sentences(0).entities = Some(entities0)
  doc.sentences(1).entities = Some(entities1)

  // extract mentions from annotated document
  val mentions = extractor extractFrom doc

  // code used for printing the found mentions
  for (m <- mentions) {
    m match {
      case m: EventMention =>
        println("EventMention")
        println(m.label)
        println(s"string = ${m.text}")
        println(s"trigger = ${m.trigger.text}")
        m.arguments foreach {
          case (k,vs) => for (v <- vs) println(s"$k = ${v.text}")
        }
        println("=" * 72)
      case m: RelationMention =>
        println("RelationMention")
        println(m.label)
        println(s"string = ${m.text}")
        m.arguments foreach {
          case (k,vs) => for (v <- vs) println(s"$k = ${v.text}")
        }
        println("=" * 72)
      case _ => ()
    }
  }
}

/**
 * Copyright (C) 2015 Typesafe Inc. <http://www.typesafe.com>
 */
package akka.persistence.query.scaladsl

import akka.NotUsed
import akka.stream.scaladsl.Source

/**
 * A plugin may optionally support this query by implementing this trait.
 */
trait AllPersistenceIdsQuery extends ReadJournal {

  /**
   * Query all `PersistentActor` identifiers, i.e. as defined by the
   * `persistenceId` of the `PersistentActor`.
   *
   * The stream is not completed when it reaches the end of the currently used `persistenceIds`,
   * but it continues to push new `persistenceIds` when new persistent actors are created.
   * Corresponding query that is completed when it reaches the end of the currently
   * currently used `persistenceIds` is provided by [[CurrentPersistenceIdsQuery#currentPersistenceIds]].
   */
  def allPersistenceIds(): Source[String, NotUsed]

}

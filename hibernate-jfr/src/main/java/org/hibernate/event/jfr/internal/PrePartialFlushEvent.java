/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.event.jfr.internal;

import org.hibernate.event.spi.HibernateMonitoringEvent;
import org.hibernate.internal.build.AllowNonPortable;

import jdk.jfr.Category;
import jdk.jfr.Description;
import jdk.jfr.Event;
import jdk.jfr.Label;
import jdk.jfr.Name;
import jdk.jfr.StackTrace;

@Name( PrePartialFlushEvent.NAME )
@Label( "PrePartialFlushEvent Execution" )
@Category( "Hibernate ORM" )
@Description( "PrePartialFlushEvent Execution" )
@StackTrace(false)
@AllowNonPortable
public class PrePartialFlushEvent extends Event implements HibernateMonitoringEvent {
	public static final String NAME = "org.hibernate.orm.PrePartialFlushEvent";

	@Label("Session Identifier")
	public String sessionIdentifier;

	@Override
	public String toString() {
		return NAME;
	}

}

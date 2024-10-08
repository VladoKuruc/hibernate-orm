/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or http://www.gnu.org/licenses/lgpl-2.1.html
 */
package org.hibernate.stat.internal;

import org.hibernate.metamodel.model.domain.NavigableRole;
import org.hibernate.persister.entity.EntityPersister;

/**
 * Utilities useful when dealing with stats.
 *
 * @author Steve Ebersole
 */
public class StatsHelper {

	public static NavigableRole getRootEntityRole(EntityPersister entityDescriptor) {
		final String rootEntityName = entityDescriptor.getRootEntityName();
		if ( entityDescriptor.getEntityName().equals( rootEntityName ) ) {
			return entityDescriptor.getNavigableRole();
		}
		else {
			return entityDescriptor.getFactory().getMappingMetamodel()
					.getEntityDescriptor( rootEntityName )
					.getNavigableRole();
		}
	}

	private StatsHelper() {
	}
}

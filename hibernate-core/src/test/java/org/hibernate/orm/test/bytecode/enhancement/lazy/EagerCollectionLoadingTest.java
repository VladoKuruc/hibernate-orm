/*
 * SPDX-License-Identifier: LGPL-2.1-or-later
 * Copyright Red Hat Inc. and Hibernate Authors
 */
package org.hibernate.orm.test.bytecode.enhancement.lazy;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.cfg.AvailableSettings;
import org.hibernate.proxy.HibernateProxy;

import org.hibernate.testing.bytecode.enhancement.extension.BytecodeEnhanced;
import org.hibernate.testing.orm.junit.DomainModel;
import org.hibernate.testing.orm.junit.JiraKey;
import org.hibernate.testing.orm.junit.ServiceRegistry;
import org.hibernate.testing.orm.junit.SessionFactory;
import org.hibernate.testing.orm.junit.SessionFactoryScope;
import org.hibernate.testing.orm.junit.Setting;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hibernate.Hibernate.isInitialized;
import static org.hibernate.Hibernate.isPropertyInitialized;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SuppressWarnings("JUnitMalformedDeclaration")
@JiraKey("HHH-10186")
@DomainModel(
		annotatedClasses = {
				EagerCollectionLoadingTest.Parent.class, EagerCollectionLoadingTest.Child.class
		}
)
@ServiceRegistry(
		settings = {
				@Setting( name = AvailableSettings.USE_SECOND_LEVEL_CACHE, value = "false" ),
				@Setting( name = AvailableSettings.ENABLE_LAZY_LOAD_NO_TRANS, value = "true" ),
		}
)
@SessionFactory
@BytecodeEnhanced
public class EagerCollectionLoadingTest {
	private static final int CHILDREN_SIZE = 10;
	private Long parentID;
	private Parent parent;

	@BeforeEach
	public void prepare(SessionFactoryScope scope) {
		scope.inTransaction( s -> {
			Parent parent = new Parent();
			parent.setChildren( new ArrayList<>() );
			for ( int i = 0; i < CHILDREN_SIZE; i++ ) {
				Child child = new Child();
				child.parent = parent;
				s.persist( child );
			}
			s.persist( parent );
			parentID = parent.id;
		} );
	}

	@Test
	public void testTransaction(SessionFactoryScope scope) {
		scope.inTransaction( s -> {
			Parent parent = s.find( Parent.class, parentID );
			assertThat( parent, notNullValue() );
			assertThat( parent, not( instanceOf( HibernateProxy.class ) ) );
			assertTrue( isPropertyInitialized( parent, "children" ) );

			List children1 = parent.children;
			List children2 = parent.children;

			//checkDirtyTracking( parent );

			assertThat( children1, sameInstance( children2 ) );

			assertThat( children1.size(), equalTo( CHILDREN_SIZE ) );
			assertTrue( isInitialized( children1 ) );
		} );
	}

	@Test
	public void testNoTransaction(SessionFactoryScope scope) {
		scope.inTransaction( s -> {
			parent = s.find( Parent.class, parentID );
			assertThat( parent, notNullValue() );
			assertThat( parent, not( instanceOf( HibernateProxy.class ) ) );
			assertTrue( isPropertyInitialized( parent, "children" ) );
		} );

		List children1 = parent.children;
		List children2 = parent.children;

		//checkDirtyTracking( parent );
		assertThat( children1, sameInstance( children2 ) );

		assertThat( children1.size(), equalTo( CHILDREN_SIZE ) );
		assertTrue( isInitialized( children1 ) );
	}

	// --- //

	@Entity
	@Table( name = "PARENT" )
	static class Parent {

		@Id
		@GeneratedValue( strategy = GenerationType.AUTO )
		Long id;

		@OneToMany( mappedBy = "parent", cascade = CascadeType.ALL, fetch = FetchType.EAGER )
		List<Child> children;

		void setChildren(List<Child> children) {
			this.children = children;
		}
	}

	@Entity
	@Table( name = "CHILD" )
	static class Child {

		@Id
		@GeneratedValue( strategy = GenerationType.AUTO )
		Long id;

		@ManyToOne( cascade = CascadeType.ALL, fetch = FetchType.LAZY )
		Parent parent;

		String name;

		Child() {
		}

		Child(String name) {
			this.name = name;
		}
	}
}

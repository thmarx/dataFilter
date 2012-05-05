dataFilter
==========

Maven repository
---------

``` xml
<repository>
	<id>marx-labs</id>
	<name>marx-labs components</name>
	<url>http://marx-labs.googlecode.com/svn/repo/</url>
</repository>
```


Maven dependency: 
-------------
``` xml
<dependency>
	<groupId>net.mad.data</groupId>
	<artifactId>datafilter</artifactId>
	<version>0.1</version>
</dependency>
```


Usage
-----

``` java
/*
Class
*/
class Person {
	public String name;
	public int age;
}

// Create DataFilter for the class
DataFilter<Person> personFilter = DataFilter.builder(Person.class).build();
// add one or more persons
personFilter.add(person);
personFilter.addAll(personCollection);

// a dimension is a simple lookup from the wanted attribute to a object

/* 
create dimension for the name attribute of the person class
*/
Dimension<String, Person> nameDim = personFilter.dimension(new ValueAccessorFunktion<Person, String>() {
	public String apply(Person type) {
		return type.name;
	}
}, String.class);

/* 
create dimension for the age attribute of the person class
*/
Dimension<Integer, Person> ageDim = personFilter.dimension(new ValueAccessorFunktion<Person, Integer>() {
	public Integer apply(Person type) {
		return type.age;
	}
}, Integer.class);


// access all persons with name eq Peter
nameDim.filterExact("Peter");

// access all persons who are 20 years old
ageDim.filterExact(20);

// access all persons who are between 20 and 30 years old
ageDim.filterRange(20, 30);
```
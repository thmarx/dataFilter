dataFilter
==========


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
Dimension<String, Person> nameDim = personFilter.dimension(new ValueFunktion<Person, String>() {
	public String value(Person type) {
		return type.name;
	}
}, String.class);

/* 
create dimension for the age attribute of the person class
*/
Dimension<Integer, Person> ageDim = personFilter.dimension(new ValueFunktion<Person, Integer>() {
	public Integer value(Person type) {
		return type.age;
	}
}, Integer.class);


// access all persons with name eq Peter
nameDim.filterExact("Peter");

// access all persons who are 20 years old
ageDim.filterExact(20);

// access all persons who are between 20 and 30 years old
ageDim.filterRange(20, 30);



/*
none blocking mode to create dimension
*/
df.dimension(new ValueFunktion<Person, String>() {

					public String value(Person type) {
						return type.name;
					}
				}, Integer.class, new ReturnFunction<Dimension<String, Person>>() {
			@Override
			public void handle(Dimension<String, Person> dimension) {
				nameDim.filterExact("Peter");
			}
		}
		);

/*
none blocking mode on filtering (filterExact)
*/
nameDim.filter("Peter", new ReturnFunction<Collection<Person>>() {
			@Override
			public void handle(Collection<Person> elements) {
				...
			}
		});


```

Performance
----------

default
-------
``` java
DataFilter.builder(Person.class).build();
```
start[1336383208983] time[0] tag[createDateFilter 1000]

start[1336383208984] time[1] tag[testCreateNameDimesion_1000]

start[1336383208992] time[10] tag[testCreateNameDimesion_10000]

start[1336383201790] time[251] tag[testCreateNameDimesion_100000]

start[1336383202720] time[1467] tag[testCreateNameDimesion_500000]

start[1336383205579] time[3402] tag[testCreateNameDimesion_1000000]



synched
-------
``` java
DataFilter.builder(Person.class).synched(true).build();
```
start[1336383242113] time[4] tag[createDateFilter 1000]

start[1336383242129] time[19] tag[testCreateNameDimesion_1000]

start[1336383242171] time[77] tag[testCreateNameDimesion_10000]

start[1336383242434] time[379] tag[testCreateNameDimesion_100000]

start[1336383243520] time[2851] tag[testCreateNameDimesion_500000]

start[1336383247766] time[6593] tag[testCreateNameDimesion_1000000]


parallel + synched
------------------
``` java
DataFilter.builder(Person.class).parallel(true).build();
```
start[1336383270365] time[3] tag[createDateFilter 1000]

start[1336383270378] time[19] tag[testCreateNameDimesion_1000]

start[1336383270417] time[57] tag[testCreateNameDimesion_10000]

start[1336383270582] time[234] tag[testCreateNameDimesion_100000]

start[1336383271499] time[971] tag[testCreateNameDimesion_500000]

start[1336383273839] time[2314] tag[testCreateNameDimesion_1000000]

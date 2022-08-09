![](https://img.shields.io/badge/-Blueprint-orange?style=for-the-badge&logo=appveyor)

A library to aid in the abstraction of encoding/decoding objects to/from multiple data formats.

- **How do I import the library?**

  The library is published on Maven, and should be added using Gradle.
    ```groovy
    repositories {
        maven {
            name = "vini2003.dev"
            url = "https://maven.vini2003.dev/"
        }
    }
  
    dependencies {
        implementation 'dev.vini2003:blueprint:0.1.11'
    } 
    ```

  Default parsers are available for the following environments:


  - **Fabric** (**BufParser**, **NbtParser**, **JsonParser**)
    ```groovy
      implementation 'dev.vini2003:blueprint-fabric:0.1.11'
    ```
    
  - **Paper** (**BufParser**, **NbtParser**, **JsonParser**)
    ```groovy
      implementation 'dev.vini2003:blueprint-paper:0.1.11'
    ```

  - **GSON** (**JsonParser**)
    ```groovy
      implementation 'dev.vini2003:blueprint-gson:0.1.11'
    ```
  
  - **Netty** (**BufParser**)
    ```groovy
      implementation 'dev.vini2003:blueprint-netty:0.1.11'
    ```
    
  
- **How do I use the library?**

The library allows encoding/decoding objects through what are called **Blueprint**s. 
A **Blueprint** describes how an object should be encoded and decoded in an abstract way, utilizing a **Serializer** or a **Deserializer**. 

The submodules of the library provide **Serializer** and **Deserializer** implementations for varying formats.

- **How does a Blueprint work?**

Blueprints, by default, have a `key`, `getter` and a `setter`.
- The `key` is responsible for the name of the property when encoding/decoding. Can be set using `Blueprint#key`. The existing blueprint is **not** mutated.
- The `getter` is responsible for retrieving the value of the property from the source object when encoding. Can be set using `Blueprint#getter`. The existing blueprint is **not** mutated.
- The `setter` is responsible for setting the value of the property on the destination object when decoding. Can be set using `Blueprint#setter`. The existing blueprint is **not** mutated.


- The `key` can be set using `Blueprint#key(...)`.
- The `getter` can be set using `Blueprint#getter(...)`.
- The `setter` can be set using `Blueprint#setter(...)`.
- The `getter` and the `setter` can be set at the same time using `Blueprint#field(...)`.

There are also multiple types of blueprints, which can be used to better represent data types:
- **CompoundBlueprint**s are blueprints used to describe an arbitrary object with up to **12** fields. They are most useful when mapping an external object, since they allow complete control over how each field is encoded/decoded.
  - **CompoundBlueprint**s take a list of **Blueprint**s as their constructor parameter, and require a constructor with the same number of parameters, where the decoded values are mapped to an object.
  - Can be created with `Blueprint#compound(...)`, with up to **12** `Blueprint` parameters, which are the blueprints used to encode/decode each field.
- **MapBlueprint**s are blueprints used to describe a `Map<K, V>`, where `K` is the key type and `V` is the value type.
- **CollectionBlueprint**s are blueprints used to describe a `Collection<T>`, where `T` is the value type.
  - Can be created with `Blueprint#list(Blueprint<T> valueBlueprint)`, where `valueBlueprint` is the blueprint used to encode/decode the values.
  - Can be created with `Blueprint#set(Blueprint<T> valueBlueprint)`, where `valueBlueprint` is the blueprint used to encode/decode the values.
  - Can be created with `Blueprint#queue(Blueprint<T> valueBlueprint)`, where `valueBlueprint` is the blueprint used to encode/decode the values.
  - Can also be created by calling `Blueprint#list()` on an existing blueprint.
  - Can also be created by calling `Blueprint#set()` on an existing blueprint.
  - Can also be created by calling `Blueprint#queue()` on an existing blueprint.
- **PairBlueprint**s are blueprints used to describe a `Pair<T, U>`, where `T` is the first value type and `U` is the second value type.
  - Can be created with `Blueprint#pair(Blueprint<T> firstBlueprint, Blueprint<U> secondBlueprint)`, where `firstBlueprint` and `secondBlueprint` are the blueprints used to encode/decode the elements.
- **OptionalBlueprint**s are blueprints used to describe an `Optional<T>`, where `T` is the value type.
  - Can be created with `Blueprint#optional(Blueprint<T> valueBlueprint)`, where `valueBlueprint` is the blueprint used to encode/decode the value.
  - Can also be created by calling `Blueprint#optional()` on an existing blueprint.
- **GeneratedBlueprint**s are blueprints used to describe an arbitrary object with any number of fields, and are generated automatically. **However**, they require that a blueprint already exists for fields in the object, or that one can be created using existing blueprints, and that a getter and/or a setter is present for the fields it will encompass - fields without them are ignored. This process is recursive.
  - **GeneratedBlueprint**s are generated by annotating a class with `@Blueprintable`, and accessed by using `Blueprint#of(T t)`/`Blueprint#of(Class<T> clazz)`.
- Custom blueprints can be registered using `Blueprint#register(Class<T>, Blueprint)`.
- Custom blueprints can be created by `xmap`'ing existing blueprints. A blueprint for a UUID can be created as follows:
  - ```java
    public static final Blueprint<UUID> UUID = Blueprint.STRING.xmap(UUID::fromString, UUID::toString);
    ```
    The existing blueprint is **not** mutated.
- **One** default blueprint for a class can be registered by annotating **one** static field `static Blueprint<T>` (where `T` is the current class) with `@DefaultBlueprint`, if one cannot be generated automatically. 

For example, in order to serialize the following object:

```java
public class Person {
    private String name;
    private int age;
    
    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public int getAge() {
        return age;
    }
    
    public void setAge(int age) {
        this.age = age;
    }
}
```

The following **Blueprint** should be used:

```java
public static final Blueprint<Person> BLUEPRINT = Blueprint.compound(
    Blueprint.STRING.key("name").getter(Person::getName).setter(Person::setName),
    Blueprint.INTEGER.key("age").getter(Person::getAge).setter(Person::setAge),
    Person::new
);
```

However, since its fields have existing associated blueprints, or can have a blueprint created **using existing blueprints**, the class may be annotated with `@Blueprintable`, and a blueprint for it will automatically be generated, and can be obtained using `Blueprint#ofValue`/`Blueprint#ofClass`.

For example, in order to demonstrate automatic generation of a blueprint, follow the code snippets below.

Firstly, we create a `Job` and a `Person` class. The Job class has a `String` name and an `int` wage, and the `Person` class has a `String` name, `int` age, and a `Job` job. We annotate both with `@Blueprintable`.

```java
@Blueprintable
public static class Job {
    private String name;
    private int wage;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWage() {
        return wage;
    }

    public void setWage(int wage) {
        this.wage = wage;
    }
}

@Blueprintable
public static class Person {
    private String name;
    private int age;
    
    private Job job;

    public Person() {
    }

    public Person(String name, int age, Job job) {
        this.name = name;
        this.age = age;
        this.job = job;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }
}
```

Then, we run the following code to confirm that the blueprints were generated correctly:

```java
var job = new Job();
job.setName("Programmer");
job.setWage(3000);

System.out.println("Created Job!");
System.out.println("Job[Name[" + job.getName() + "], Wage[" + job.getWage() + "]]");

var person = new Person();
person.setName("John Doe");
person.setAge(27);
person.setJob(job);

System.out.println("Created Person");
System.out.println("Person[Name[" + person.getName() + "], Age[" + person.getAge() + "], Job[" + person.getJob() + "]]");

var personBlueprint = Blueprint.of(person);

System.out.println("Created Person Blueprint!");
System.out.println(personBlueprint);

var personJsonObject = new JsonObject();

personBlueprint.encode(JsonParser.INSTANCE, person, personJsonObject);

System.out.println("Encoded Person to JSON!");
System.out.println(personJsonObject);

person = personBlueprint.decode(JsonParser.INSTANCE, personJsonObject);

System.out.println("Decoded Person from JSON!");
System.out.println("Person[Name[" + person.getName() + "], Age[" + person.getAge() + "], Job[" + person.getJob() + "]]");
```

By doing that, we obtain the following result:

```java
Created Person Blueprint!
GeneratedBlueprint[None, [Blueprint[name], Blueprint[age], Blueprint[job]]]
Encoded Person to JSON!
{"name":"John Doe","age":27,"job":{"name":"Programmer","wage":3000}}
Decoded Person from JSON!
Person[Name[John Doe], Age[27], Job[null]]
```

Therefore, serialization was successful.
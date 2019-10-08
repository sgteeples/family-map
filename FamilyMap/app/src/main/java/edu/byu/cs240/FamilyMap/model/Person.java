package edu.byu.cs240.FamilyMap.model;

public class Person {

    private final String personID;
    private final String descendant;
    private final String firstName;
    private final String lastName;
    private final String gender;
    private String father;
    private String mother;
    private String spouse;

    public Person(String personID, String descendant, String firstName, String lastName,
                  String gender, String father, String mother, String spouse) {
        this.personID = personID;
        this.descendant = descendant;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.father = father;
        this.mother = mother;
        this.spouse = spouse;
    }

    public String getPersonID() {
        return personID;
    }

    private String getDescendant() {
        return descendant;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getGender() {
        return gender;
    }

    public String getFather() {
        return father;
    }

    public String getMother() {
        return mother;
    }

    public String getSpouse() {
        return spouse;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;
        if (o == this)
            return true;
        if (o instanceof Person) {
            Person oPerson = (Person) o;
            return oPerson.getPersonID().equals(getPersonID()) &&
                    oPerson.getDescendant().equals(getDescendant()) &&
                    oPerson.getFirstName().equals(getFirstName()) &&
                    oPerson.getLastName().equals(getLastName()) &&
                    oPerson.getGender().equals(getGender()) &&
                    oPerson.getFather().equals(getFather()) &&
                    oPerson.getMother().equals(getMother()) &&
                    oPerson.getSpouse().equals(getSpouse());
        }
        return false;
    }
}
package com.th.btvn;

public class User {
    private int id;
    private String name;
    private String username;
    private String email;
    private String phone;
    private String website;
    private Address address;
    private Company company;

    // Constructors
    public User() {}

    public User(int id, String name, String username, String email, String phone, String website) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.website = website;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getWebsite() { return website; }
    public void setWebsite(String website) { this.website = website; }

    public Address getAddress() { return address; }
    public void setAddress(Address address) { this.address = address; }

    public Company getCompany() { return company; }
    public void setCompany(Company company) { this.company = company; }

    // Nested classes for Address and Company
    public static class Address {
        private String street;
        private String suite;
        private String city;
        private String zipcode;

        public String getStreet() { return street; }
        public void setStreet(String street) { this.street = street; }

        public String getSuite() { return suite; }
        public void setSuite(String suite) { this.suite = suite; }

        public String getCity() { return city; }
        public void setCity(String city) { this.city = city; }

        public String getZipcode() { return zipcode; }
        public void setZipcode(String zipcode) { this.zipcode = zipcode; }

        public String getFullAddress() {
            return suite + " " + street + ", " + city + " " + zipcode;
        }
    }

    public static class Company {
        private String name;
        private String catchPhrase;
        private String bs;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getCatchPhrase() { return catchPhrase; }
        public void setCatchPhrase(String catchPhrase) { this.catchPhrase = catchPhrase; }

        public String getBs() { return bs; }
        public void setBs(String bs) { this.bs = bs; }
    }
}

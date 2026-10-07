package CustomerManager.model;

public class CustomerModel {
    private final String name;
    private final String province;

    public CustomerModel(String name, String province) {
        this.name = name;
        this.province = province;
    }

    public String getName() {
        return name;
    }

    public String getProvince() {
        return province;
    }
}

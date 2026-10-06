package vn.iotstar.config;


public class TestConnection {
    public static void main(String[] args) {
        System.out.println(
                JPAConfig.getEntityManager()
        );
    }
}
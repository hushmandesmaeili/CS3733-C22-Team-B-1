package edu.wpi.teamB;

import edu.wpi.cs3733.c22.teamB.entity.MongoDB.EmployeeMongo;
import edu.wpi.cs3733.c22.teamB.entity.MongoDB.EquipmentMongo;
import edu.wpi.cs3733.c22.teamB.entity.MongoDB.LocationMongo;
import edu.wpi.cs3733.c22.teamB.entity.MongoDB.MongoDB;
import edu.wpi.cs3733.c22.teamB.entity.objects.Employee;
import edu.wpi.cs3733.c22.teamB.entity.objects.Location;
import edu.wpi.cs3733.c22.teamB.entity.objects.MedicalEquipment;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.net.UnknownHostException;

import static org.junit.jupiter.api.Assertions.*;

public class MongoTest {


    public MongoTest() {

    }

    @Test
    public void testCreateLocation() throws UnknownHostException {
        MongoDB.getConnection();
        LocationMongo locationMongo = new LocationMongo();
        locationMongo.dropTable();
        locationMongo.createTable();

        Location location1 = new Location("12", 12, 12, "12", "123", "123", "21e", "q2e");
        Location location2 = new Location("123", 13, 13, "12", "123", "123", "123", "123");
        Location location3 = new Location("123", 1000, 13, "12", "123", "123", "123", "123");


        locationMongo.addValue(location1);
        locationMongo.addValue(location2);
        locationMongo.updateValue(location3);
        locationMongo.deleteValue(location1.getNodeID());
        locationMongo.getValue(location3.getNodeID());
        locationMongo.getAllValues();
    }

    @Test
    public void testEmployeeMongo() throws UnknownHostException {
        MongoDB.getConnection();
        EmployeeMongo employeeMongo = new EmployeeMongo();
        employeeMongo.dropTable();
        employeeMongo.createTable();

        Employee employee1 = new Employee("123", "123", "123", "123", 12, "123", "123", "213", "123");
        Employee employee2 = new Employee("223", "123", "123", "123", 12, "123", "123", "213", "123");
        Employee employee3 = new Employee("323", "123", "Ben", "123", 12, "123", "123", "213", "123");
        Employee employee12 = new Employee("123", "123", "Hushmand", "123", 12, "123", "123", "213", "123");

        employeeMongo.addValue(employee1);
        employeeMongo.addValue(employee2);
        employeeMongo.addValue(employee3);
        employeeMongo.updateValue(employee12);
        employeeMongo.deleteValue(employee3.getEmployeeID());

        employeeMongo.getValue(employee2.getEmployeeID());
        employeeMongo.getAllValues();
    }

    @Test
    public void testMedicalEquipmentMongo() throws UnknownHostException {
        MongoDB.getConnection();
        LocationMongo locationMongo = new LocationMongo();
        locationMongo.dropTable();
        locationMongo.createTable();

        Location location1 = new Location("12", 12, 12, "12", "123", "123", "21e", "q2e");
        Location location2 = new Location("123", 13, 13, "12", "123", "123", "123", "123");
        Location location3 = new Location("123", 1000, 13, "12", "123", "123", "123", "123");


        locationMongo.addValue(location1);
        locationMongo.addValue(location2);
        locationMongo.updateValue(location3);
        locationMongo.deleteValue(location1.getNodeID());
        locationMongo.getValue(location3.getNodeID());
        locationMongo.getAllValues();

        MongoDB.getConnection();
        EquipmentMongo equipmentMongo = new EquipmentMongo(locationMongo);
        equipmentMongo.dropTable();
        equipmentMongo.createTable();

        MedicalEquipment equipment1 = new MedicalEquipment("1", "12", "13", "12",location2, "123", "123", "123", "123",1);
        MedicalEquipment equipment2 = new MedicalEquipment("2", "12", "13", "12",location2, "123", "123", "123", "123",1);
        MedicalEquipment equipment3 = new MedicalEquipment("1", "12", "13", "12",location2, "123", "123", "123", "123",1);



        equipmentMongo.addValue(equipment1);
        equipmentMongo.addValue(equipment2);
        equipmentMongo.updateValue(equipment3);
        equipmentMongo.deleteValue(equipment1.getEquipmentID());
        equipmentMongo.getValue(equipment2.getEquipmentID());
        equipmentMongo.getAllValues();

        assertEquals(equipmentMongo.getValue(equipment2.getEquipmentID()).getLocation(),location3);

    }


}

package edu.wpi.cs3733.c22.teamB.entity;

import edu.wpi.cs3733.c22.teamB.Main;
import edu.wpi.cs3733.c22.teamB.entity.MongoDB.*;
import edu.wpi.cs3733.c22.teamB.entity.inheritance.AbstractSR;
import edu.wpi.cs3733.c22.teamB.entity.inheritance.IDatabase;
import edu.wpi.cs3733.c22.teamB.entity.objects.Employee;
import edu.wpi.cs3733.c22.teamB.entity.objects.Location;
import edu.wpi.cs3733.c22.teamB.entity.objects.MedicalEquipment;
import edu.wpi.cs3733.c22.teamB.entity.objects.services.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class DatabaseWrapper {

    private static DatabaseWrapper DatabaseWrapperInstance;

    private IDatabase<Location> LocationDao;
    private IDatabase<Location> LocationDerby;
    private IDatabase<Location> LocationMongo;

    private IDatabase<Employee> EmployeeDao;
    private IDatabase<Employee> EmployeeDerby;
    private IDatabase<Employee> EmployeeMongo;

    private IDatabase<MedicalEquipment> MedicalEquipmentDao;
    private IDatabase<MedicalEquipment> MedicalEquipmentDerby;
    private IDatabase<MedicalEquipment> MedicalEquipmentMongo;

    private IDatabase<ExternalTransportSR> ExternalTransportDao;
    private IDatabase<ExternalTransportSR> ExternalTransportDerby;
    private IDatabase<ExternalTransportSR> ExternalTransportMongo;

    private IDatabase<FoodDeliverySR> FoodDeliveryDao;
    private IDatabase<FoodDeliverySR> FoodDeliveryDerby;
    private IDatabase<FoodDeliverySR> FoodDeliveryMongo;

    private IDatabase<GiftFloralSR> GiftFloralSRDao;
    private IDatabase<GiftFloralSR> GiftFloralSRDerby;
    private IDatabase<GiftFloralSR> GiftFloralSRMongo;

    private IDatabase<LaundrySR> LaundrySRDao;
    private IDatabase<LaundrySR> LaundrySRDerby;
    private IDatabase<LaundrySR> LaundrySRMongo;

    private IDatabase<MedicalEquipmentSR> MedicalEquipmentSRDao;
    private IDatabase<MedicalEquipmentSR> MedicalEquipmentSRDerby;
    private IDatabase<MedicalEquipmentSR> MedicalEquipmentSRMongo;

    private IDatabase<MedicineDeliverySR> MedicineDeliverySRDao;
    private IDatabase<MedicineDeliverySR> MedicineDeliverySRDerby;
    private IDatabase<MedicineDeliverySR> MedicineDeliverySRMongo;

    private IDatabase<ComputerServiceSR> ComputerServiceSRDao;
    private IDatabase<ComputerServiceSR> ComputerServiceSRDerby;
    private IDatabase<ComputerServiceSR> ComputerServiceSRMongo;

    private IDatabase<SanitationSR> SanitationSRDao;
    private IDatabase<SanitationSR> SanitationSRDerby;
    private IDatabase<SanitationSR> SanitationSRMongo;

    private IDatabase<AbstractSR> MainSRDao;
    private IDatabase<AbstractSR> MainSRDerby;
    private IDatabase<AbstractSR> MainSRMongo;

    private ConnectionManager connectionManager;

    private RestoreBackupWrapper restoreBackupWrapper;

//    private boolean locationChanged = true;
//    private boolean employeeChanged = true;
//    private boolean equipmentChanged = true;
//    private boolean SRChanged = true;

//    private List<Location> locationCache;
//    private List<Employee> employeeCache;
//    private List<MedicalEquipment> equipmentCache;
//    private List<AbstractSR> SRCache;

    private DatabaseWrapper() {

        MongoDB.getConnection();

        this.LocationDerby = new LocationDaoI();
        this.LocationMongo = new LocationMongo();
        this.LocationDao = LocationDerby;

        this.EmployeeDerby = new EmployeeDaoI();
        this.EmployeeMongo = new EmployeeMongo();
        this.EmployeeDao = EmployeeDerby;

        this.MedicalEquipmentDerby = new MedicalEquipmentDaoI();
        this.MedicalEquipmentMongo = new EquipmentMongo(this.LocationMongo);
        this.MedicalEquipmentDao = MedicalEquipmentDerby;

        this.MainSRDerby = new MainSRDaoI();
        this.MainSRMongo = new MainSRMongo(this.LocationMongo, this.EmployeeMongo);
        this.MainSRDao = MainSRDerby;

        this.ExternalTransportDerby = new ExternalTransportSRDaoI();
        this.ExternalTransportMongo = new ExternalTransportSRMongo(this.MainSRMongo);
        this.ExternalTransportDao = ExternalTransportDerby;

        this.FoodDeliveryDerby = new FoodDeliverySRDaoI();
        this.FoodDeliveryMongo = new FoodDeliverySRMongo(this.MainSRMongo);
        this.FoodDeliveryDao = FoodDeliveryDerby;

        this.GiftFloralSRDerby = new GiftFloralSRDaoI();
        this.GiftFloralSRMongo = new GiftFloralSRMongo(this.MainSRMongo);
        this.GiftFloralSRDao = GiftFloralSRDerby;

        this.LaundrySRDerby = new LaundrySRDaoI();
        this.LaundrySRMongo = new LaundrySRMongo(this.MainSRMongo);
        this.LaundrySRDao = LaundrySRDerby;

        this.MedicalEquipmentSRDerby = new MedicalEquipmentSRDaoI();
        this.MedicalEquipmentSRMongo = new MedicalEquipmentSRMongo(this.MainSRMongo, this.MedicalEquipmentMongo);
        this.MedicalEquipmentSRDao = MedicalEquipmentSRDerby;

        this.MedicineDeliverySRDerby = new MedicineDeliverySRDaoI();
        this.MedicineDeliverySRMongo = new MedicineDeliverySRMongo(this.MainSRMongo);
        this.MedicineDeliverySRDao = MedicineDeliverySRDerby;

        this.ComputerServiceSRDerby = new ComputerServiceSRDaoI();
        this.ComputerServiceSRMongo = new ComputerServiceSRMongo(this.MainSRDao);
        this.ComputerServiceSRDao = ComputerServiceSRDerby;

        this.SanitationSRDerby = new SanitationSRDaoI();
        this.SanitationSRMongo = new SanitationSRMongo(this.MainSRMongo);
        this.SanitationSRDao = SanitationSRDerby;

        connectionManager = ConnectionManager.getInstance();
        restoreBackupWrapper = new RestoreBackupWrapper(LocationDao, EmployeeDao, MedicalEquipmentDao, MainSRDao,
                ExternalTransportDao, FoodDeliveryDao, GiftFloralSRDao, LaundrySRDao, MedicalEquipmentSRDao,
                MedicineDeliverySRDao, ComputerServiceSRDao, SanitationSRDao);

//        locationCache = getAllLocation();
//        employeeCache = getAllEmployee();
//        equipmentCache = getAllMedicalEquipment();
//        SRCache = getAllSR();
    }

    public static DatabaseWrapper getInstance() {
        if (DatabaseWrapperInstance == null) {
            DatabaseWrapperInstance = new DatabaseWrapper();
        }

        return DatabaseWrapperInstance;
    }

    public void engageEmbedded() {
        try {
            backupAll();
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.LocationDao = this.LocationDerby;
        this.EmployeeDao = this.EmployeeDerby;
        this.MedicalEquipmentDao = this.MedicalEquipmentDerby;
        this.MainSRDao = this.MainSRDerby;
        this.ExternalTransportDao = this.ExternalTransportDerby;
        this.FoodDeliveryDao = this.FoodDeliveryDerby;
        this.GiftFloralSRDao = this.GiftFloralSRDerby;
        this.LaundrySRDao = this.LaundrySRDerby;
        this.MedicalEquipmentSRDao = this.MedicalEquipmentSRDerby;
        this.MedicineDeliverySRDao = this.MedicineDeliverySRDerby;
        this.ComputerServiceSRDao = this.ComputerServiceSRDerby;
        this.SanitationSRDao = this.SanitationSRDerby;

        setRestoreBackupDao();

        initEmbedded();
        try {
            restoreAll();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void engageClient() {
        try {
            backupAll();
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.LocationDao = this.LocationDerby;
        this.EmployeeDao = this.EmployeeDerby;
        this.MedicalEquipmentDao = this.MedicalEquipmentDerby;
        this.MainSRDao = this.MainSRDerby;
        this.ExternalTransportDao = this.ExternalTransportDerby;
        this.FoodDeliveryDao = this.FoodDeliveryDerby;
        this.GiftFloralSRDao = this.GiftFloralSRDerby;
        this.LaundrySRDao = this.LaundrySRDerby;
        this.MedicalEquipmentSRDao = this.MedicalEquipmentSRDerby;
        this.MedicineDeliverySRDao = this.MedicineDeliverySRDerby;
        this.ComputerServiceSRDao = this.ComputerServiceSRDerby;
        this.SanitationSRDao = this.SanitationSRDerby;

        setRestoreBackupDao();

        initClient();
        try {
            restoreAll();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void engageRemote() {

        try {
            backupAll();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.LocationDao = this.LocationMongo;
        this.EmployeeDao = this.EmployeeMongo;
        this.MedicalEquipmentDao = this.MedicalEquipmentMongo;
        this.MainSRDao = this.MainSRMongo;
        this.ExternalTransportDao = this.ExternalTransportMongo;
        this.FoodDeliveryDao = this.FoodDeliveryMongo;
        this.GiftFloralSRDao = this.GiftFloralSRMongo;
        this.LaundrySRDao = this.LaundrySRMongo;
        this.MedicalEquipmentSRDao = this.MedicalEquipmentSRMongo;
        this.MedicineDeliverySRDao = this.MedicineDeliverySRMongo;
        this.ComputerServiceSRDao = this.ComputerServiceSRMongo;
        this.SanitationSRDao = this.SanitationSRMongo;

        setRestoreBackupDao();

        try {
            restoreAll();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initEmbedded() {
        connectionManager.setConnectionStrategy(false);
    }

    public void initClient() {
        connectionManager.setConnectionStrategy(true);
    }

    private void setRestoreBackupDao() {
        restoreBackupWrapper.setDao(LocationDao, EmployeeDao, MedicalEquipmentDao, MainSRDao,
                ExternalTransportDao, FoodDeliveryDao, GiftFloralSRDao, LaundrySRDao, MedicalEquipmentSRDao,
                MedicineDeliverySRDao, ComputerServiceSRDao, SanitationSRDao);
    }

    public void addSR(AbstractSR abstractSR) {
        MainSRDao.addValue(abstractSR); //TODO do you need this or comment out?ExternalTransportDao.addValue(abstractSR);
        System.out.println(abstractSR.getSrType());
        switch (abstractSR.getSrType()) {
            case "ExternalTransportSR":
                ExternalTransportDao.addValue((ExternalTransportSR) abstractSR);
                break;
            case "FoodDeliverySR":
                FoodDeliveryDao.addValue((FoodDeliverySR) abstractSR);
                break;
            case "GiftFloralSR":
                GiftFloralSRDao.addValue((GiftFloralSR) abstractSR);
                break;
            case "LaundrySR":
                LaundrySRDao.addValue((LaundrySR) abstractSR);
                break;
            case "MedicalEquipmentSR":
                MedicalEquipmentSRDao.addValue((MedicalEquipmentSR) abstractSR);
                break;
            case "MedicineDeliverySR":
                MedicineDeliverySRDao.addValue((MedicineDeliverySR) abstractSR);
                break;
            case "ComputerServiceSR":
                ComputerServiceSRDao.addValue((ComputerServiceSR) abstractSR);
                break;
            case "SanitationSR":
                SanitationSRDao.addValue((SanitationSR) abstractSR);
                break;
            default:
                System.out.println("Invalid SR Input: " + abstractSR.getSrType());
        }

    }

    public void addLocation(Location location) {
        LocationDao.addValue(location);
//        locationChanged = true;
//        equipmentChanged = true;
//        SRChanged = true;
    }

    public void addEmployee(Employee employee) {
        EmployeeDao.addValue(employee);
//        employeeChanged = true;
//        SRChanged = true;
    }

    public void addMedicalEquipment(MedicalEquipment medicalEquipment) {
        MedicalEquipmentDao.addValue(medicalEquipment);
//        equipmentChanged = true;
//        SRChanged = true;
    }

    public void deleteSR(String srID) {

        AbstractSR abstractSR = getSR(srID);
        System.out.println(abstractSR.getSrType());
        switch (abstractSR.getSrType()) {
            case "ExternalTransportSR":
                ExternalTransportDao.deleteValue(srID);
                break;
            case "FoodDeliverySR":
                FoodDeliveryDao.deleteValue(srID);
                break;
            case "GiftFloralSR":
                GiftFloralSRDao.deleteValue(srID);
                break;
            case "LaundrySR":
                LaundrySRDao.deleteValue(srID);
                break;
            case "MedicalEquipmentSR":
                MedicalEquipmentSRDao.deleteValue(srID);
                break;
            case "MedicineDeliverySR":
                MedicineDeliverySRDao.deleteValue(srID);
                break;
            case "ComputerServiceSR":
                ComputerServiceSRDao.deleteValue(srID);
                break;
            case "SanitationSR":
                SanitationSRDao.deleteValue(srID);
            default:
                System.out.println("Invalid SRID Input: " + abstractSR.getSrID());
        }
        MainSRDao.deleteValue(srID);

//        SRChanged = true;

    }

    public void deleteLocation(String locationID) {
        LocationDao.deleteValue(locationID);
//        locationChanged = true;
//        equipmentChanged = true;
//        SRChanged = true;
    }

    public void deleteEmployee(String employeeID) {
        EmployeeDao.deleteValue(employeeID);
//        employeeChanged = true;
//        SRChanged = true;
    }

    public void deleteMedicalEquipment(String medicalEquipmentID) {
        MedicalEquipmentDao.deleteValue(medicalEquipmentID);
//        equipmentChanged = true;
//        SRChanged = true;
    }

    public void updateSR(AbstractSR abstractSR) {
        MainSRDao.updateValue(abstractSR); //TODO do you need this or comment out?ExternalTransportDao.addValue(abstractSR);
        System.out.println(abstractSR.getSrType());
        switch (abstractSR.getSrType()) {
            case "ExternalTransportSR":
                ExternalTransportDao.updateValue((ExternalTransportSR) abstractSR);
                break;
            case "FoodDeliverySR":
                FoodDeliveryDao.updateValue((FoodDeliverySR) abstractSR);
                break;
            case "GiftFloralSR":
                GiftFloralSRDao.updateValue((GiftFloralSR) abstractSR);
                break;
            case "LaundrySR":
                LaundrySRDao.updateValue((LaundrySR) abstractSR);
                break;
            case "MedicalEquipmentSR":
                MedicalEquipmentSRDao.updateValue((MedicalEquipmentSR) abstractSR);
                break;
            case "MedicineDeliverySR":
                MedicineDeliverySRDao.updateValue((MedicineDeliverySR) abstractSR);
                break;
            case "ComputerServiceSR":
                ComputerServiceSRDao.updateValue((ComputerServiceSR) abstractSR);
                break;
            case "SanitationSR":
                SanitationSRDao.updateValue((SanitationSR) abstractSR);
            default:
                System.out.println("Invalid SR Input: " + abstractSR.getSrType());
        }
//        SRChanged = true;
    }

    public void updateLocation(Location location) {
        LocationDao.updateValue(location);
//        locationChanged = true;
//        equipmentChanged = true;
//        SRChanged = true;
        }

    public void updateEmployee(Employee employee) {
        EmployeeDao.updateValue(employee);
//        employeeChanged = true;
//        SRChanged = true;
    }

    public void updateMedicalEquipment(MedicalEquipment medicalEquipment) {
        MedicalEquipmentDao.updateValue(medicalEquipment);
//        equipmentChanged = true;
//        SRChanged = true;
    }

    public AbstractSR getSR(String srID) {

        AbstractSR abstractSR = MainSRDao.getValue(srID);
        if (abstractSR != null) {
            switch (abstractSR.getSrType()) {
                case "ExternalTransportSR":
                    return ExternalTransportDao.getValue(srID);
                case "FoodDeliverySR":
                    return FoodDeliveryDao.getValue(srID);
                case "GiftFloralSR":
                    return GiftFloralSRDao.getValue(srID);
                case "LaundrySR":
                    return LaundrySRDao.getValue(srID);
                case "MedicalEquipmentSR":
                    return MedicalEquipmentSRDao.getValue(srID);
                case "MedicineDeliverySR":
                    return MedicineDeliverySRDao.getValue(srID);
                case "ComputerServiceSR":
                    return ComputerServiceSRDao.getValue(srID);
                case "SanitationSR":
                    return SanitationSRDao.getValue(srID);
                default:
                    System.out.println("Invalid SR Input: " + abstractSR.getSrType());
            }
        }
        return null;
    }

    public Location getLocation(String locationID) {
        return LocationDao.getValue(locationID);
    }

    public Employee getEmployee(String employeeID) {
        return EmployeeDao.getValue(employeeID);
    }

    public MedicalEquipment getMedicalEquipment(String medicalEquipmentID) {
        return MedicalEquipmentDao.getValue(medicalEquipmentID);
    }

    public List<AbstractSR> getAllSR() {
//        if (SRChanged == true){
            List<AbstractSR> list = MainSRDao.getAllValues();

//            for (AbstractSR abstractSR : list) {
//                abstractSR = getSR(abstractSR.getSrID());
//            }
//            SRChanged = false;
//            SRCache = list;
//        }
//        return SRCache;
        return list;
    }

    public List<Location> getAllLocation() {
//        if (locationChanged) {
//            locationCache = LocationDao.getAllValues();
        return LocationDao.getAllValues();
//            locationChanged = false;
//        }
//        return locationCache;
    }

    public List<Employee> getAllEmployee() {
//        if (employeeChanged){
//            employeeCache =
        return EmployeeDao.getAllValues();
//            employeeChanged = false;
        }
//        return employeeCache;
//    }

    public List<MedicalEquipment> getAllMedicalEquipment() {
//        if (equipmentChanged){
//            equipmentCache =
        return MedicalEquipmentDao.getAllValues();
//            equipmentChanged = false;
        }
//        return equipmentCache;
//    }

    public void createTableSR() {
        MainSRDao.createTable();
        LaundrySRDao.createTable();
        ExternalTransportDao.createTable();
        FoodDeliveryDao.createTable();
        GiftFloralSRDao.createTable();
        ComputerServiceSRDao.createTable();
        SanitationSRDao.createTable();
        MedicineDeliverySRDao.createTable();
        MedicalEquipmentSRDao.createTable();
    }

    public void createTableLocation() {
        LocationDao.createTable();
    }

    public void createTableEmployee() {
        EmployeeDao.createTable();
    }

    public void createTableMedicalEquipment() {
        MedicalEquipmentDao.createTable();
    }

    public void createAll() {
        createTableLocation();
        createTableEmployee();
        createTableMedicalEquipment();
        createTableSR();
    }

    public void dropTableSR() {
        MedicalEquipmentSRDao.dropTable();
        MedicineDeliverySRDao.dropTable();
        GiftFloralSRDao.dropTable();
        FoodDeliveryDao.dropTable();
        SanitationSRDao.dropTable();
        ComputerServiceSRDao.dropTable();
        ExternalTransportDao.dropTable();
        LaundrySRDao.dropTable();
        MainSRDao.dropTable();
    }

    public void dropTableLocation() {
        LocationDao.dropTable();
    }

    public void dropTableEmployee() {
        EmployeeDao.dropTable();
    }

    public void dropTableMedicalEquipment() {
        MedicalEquipmentDao.dropTable();
    }

    public void dropAll() {

        dropTableSR();
        dropTableMedicalEquipment();
        dropTableEmployee();
        dropTableLocation();
    }

    void restoreTableSR() throws IOException {
        restoreBackupWrapper.restoreMainSR();
        restoreBackupWrapper.restoreExternalTransportSR();
        restoreBackupWrapper.restoreFoodDeliverySR();
        restoreBackupWrapper.restoreGiftFloralSR();
        restoreBackupWrapper.restoreLaundrySR();
        restoreBackupWrapper.restoreMedicalEquipmentSR();
        restoreBackupWrapper.restoreMedicineDeliverySR();
        restoreBackupWrapper.restoreSanitationSR();
    }

    void restoreTableLocation() throws IOException {
        restoreBackupWrapper.restoreLocation();
    }

    void restoreTableEmployee() throws IOException {
        restoreBackupWrapper.restoreEmployee();
    }

    void restoreTableMedicalEquipment() throws IOException {
        restoreBackupWrapper.restoreMedicalEquipment();
    }

    public void restoreAll() throws IOException {
        System.out.println("Restore" + ConnectionManager.getInstance().getConnection());
        dropAll();
        createAll();
        restoreBackupWrapper.restoreAll();

    }

    void backupTableLocation() throws IOException {
        restoreBackupWrapper.backupLocation();
    }

    void backupTableEmployee() throws IOException {
        restoreBackupWrapper.backupEmployee();
    }

    void backupTableMedicalEquipment() throws IOException {
        restoreBackupWrapper.backupMedicalEquipment();
    }

    void backupTableSR() throws FileNotFoundException {
        restoreBackupWrapper.backupMainSR();
        restoreBackupWrapper.backupExternalTransportSR();
        restoreBackupWrapper.backupFoodDeliverySR();
        restoreBackupWrapper.backupGiftFloralSR();
        restoreBackupWrapper.backupLaundrySR();
        restoreBackupWrapper.backupMedicalEquipmentSR();
        restoreBackupWrapper.backupMedicineDeliverySR();
        restoreBackupWrapper.backupComputerServiceSR();
        restoreBackupWrapper.backupSanitationSR();
    }

    public void backupAll() throws IOException {
        restoreBackupWrapper.backupAll();
    }

    public void firstRestore() throws IOException {
        restoreBackupWrapper.firstRestore();
        createAll();
    }

    // Clean up for Iteration 3
    public void isFirstRestore() throws IOException {

        LocationDaoI test = new LocationDaoI();
        if (test.isFirstRestore()) {
            firstRestore();
        } else {
            System.out.println("Not First Restore!");
        }
    }

    // Clean up for Iteration 3
    public boolean isInTableLocation(String nodeID) {
        LocationDaoI test = new LocationDaoI();
        return test.isInTable(nodeID);
    }

    // Clean up for Iteration 3
    public int nodeTypeCountLocation(String nodeType, String floor) {
        LocationDaoI locationDaoI = new LocationDaoI();
        System.out.println(locationDaoI.nodeTypeCount(nodeType, floor));
        return locationDaoI.nodeTypeCount(nodeType, floor);
    }

    private Connection getConnection() {
        return ConnectionManager.getInstance().getConnection();
    }

    public boolean isEmbedded() {
        return (getConnection() instanceof org.apache.derby.impl.jdbc.EmbedConnection);
    }

    public boolean isClient() {
        return (getConnection() instanceof org.apache.derby.client.net.NetConnection);
    }

    public boolean isRemote() {
        return (this.LocationDao instanceof LocationMongo);
    }

    public boolean srIsInTable(String srID) {

        List<AbstractSR> mainSRList = MainSRDao.getAllValues();

        for (AbstractSR mainSR : mainSRList) {
            String mainSRID = mainSR.getSrID();
            if (mainSRID.equals(srID)) {
                return true;
            }
        }
        return false;
    }

    public IDatabase<Location> modeLocation() {
        return this.LocationDao;
    }

    public boolean isLocationReferenced(String objectID) {
        boolean isReferenced = true;
        if (this.LocationDao instanceof LocationMongo) {
            isReferenced = ((LocationMongo) this.LocationDao).isLocationReferenced(objectID);
        }

        return isReferenced;
    }

    public void closeConnection() {
        if (this.LocationDao instanceof LocationMongo) {
            MongoDB.closeConnection();
        }
    }
}


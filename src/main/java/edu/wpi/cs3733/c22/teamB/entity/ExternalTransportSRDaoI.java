package edu.wpi.cs3733.c22.teamB.entity;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ExternalTransportSRDaoI implements IDatabase<ExternalTransportSR> {

    private Connection conn;

    public ExternalTransportSRDaoI() {
        this.conn = DBConnection.getConnection();
    }

    @Override
    public void addValue(ExternalTransportSR object) {

        try {
            PreparedStatement pstmt = conn.prepareStatement("INSERT INTO EXTERNALTRANSPORTSR (srID, patientID, dropOffLocation, FORMOFTRANSPORT) VALUES(?, ?, ?, ?)");
            pstmt.setString(1, object.getSrID());
            pstmt.setString(2, object.getPatientID());
            pstmt.setString(3, object.getDropOffLocation());
            pstmt.setString(4, object.getFormOfTransport());

            pstmt.executeUpdate();
            pstmt.close();

        } catch (SQLException e) {
            System.out.println("Insert Into EXTERNALTRANSPORTSR Table: Failed!");
            e.printStackTrace();
        }
    }

    @Override
    public void deleteValue(String objectID) {

        try {
            PreparedStatement pstmt = conn.prepareStatement("DELETE FROM EXTERNALTRANSPORTSR WHERE srID = ?");
            pstmt.setString(1, objectID);
            pstmt.executeUpdate();

            pstmt.close();
        } catch (SQLException e) {
            System.out.println("Delete From EXTERNALTRANSPORTSR Table Using SR ID: Failed!");
            e.printStackTrace();
        }
    }

    @Override
    public void updateValue(ExternalTransportSR object) {

        try {
            PreparedStatement pstmt =
                    conn.prepareStatement(
                            "UPDATE EXTERNALTRANSPORTSR SET patientID = ?, dropOffLocation = ?, FORMOFTRANSPORT = ? WHERE srID = ?");

            pstmt.setString(1, object.getPatientID());
            pstmt.setString(2, object.getDropOffLocation());
            pstmt.setString(3, object.getFormOfTransport());
            pstmt.setString(4, object.getSrID());


            pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException e) {
            System.out.println("Update EXTERNALTRANSPORTSR Node: Failed!");
            e.printStackTrace();
            return;
        }
    }

    @Override
    public ExternalTransportSR getValue(String objectID) {
        ExternalTransportSR externalTransportSR = new ExternalTransportSR();

        try {
            PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM EXTERNALTRANSPORTSR JOIN MAINSR ON EXTERNALTRANSPORTSR.SRID = MAINSR.SRID");
//            pstmt.setString(1, objectID);
            ResultSet rset = pstmt.executeQuery();

            rset.next();

            String status = rset.getString("status");
            String LocationID = rset.getString("locationID");

            LocationDaoI locationDaoI = new LocationDaoI();
            Location location = locationDaoI.getValue(LocationID);

            String requestorID = rset.getString("requestorID");
            String assignedEmployeeID = rset.getString("assignedEmployeeID");

            EmployeeDaoI employeeDaoI = new EmployeeDaoI();
            Employee requestor = employeeDaoI.getValue(requestorID);
            Employee assignedEmployee = employeeDaoI.getValue(assignedEmployeeID);

            String dateRequested = rset.getString("dateRequested");
            LocalDate date = LocalDate.parse(dateRequested);
            String notes = rset.getString("notes");
            String patientID = rset.getString("patientID");
            String dropOffLocation = rset.getString("dropOffLocation");
            String formOfTransport = rset.getString("formOfTransport");

            externalTransportSR = new ExternalTransportSR(objectID, status, location, requestor, assignedEmployee, date, notes, patientID, dropOffLocation, formOfTransport);

            pstmt.close();
        } catch (SQLException e) {
            System.out.println("Get EXTERNALTRANSPORTSR Node Failed");
            e.printStackTrace();
        }
        return externalTransportSR;

    }

    @Override
    public List<ExternalTransportSR> getAllValues() {
        List<ExternalTransportSR> externalTransportSRList = new ArrayList<>();
        try{
            PreparedStatement pstmt =
                    conn.prepareStatement("SELECT SRID FROM EXTERNALTRANSPORTSR ");
            ResultSet rset = pstmt.executeQuery();


            while(rset.next()){
                externalTransportSRList.add(getValue(rset.getString("SRID")));
            }
        } catch (SQLException e) {
            System.out.println("Get EXTERNALTRANSPORTSR Node Failed");
            e.printStackTrace();
        }
        return externalTransportSRList;
    }

    @Override
    public void createTable() {
        try {
            DatabaseMetaData dbmd = conn.getMetaData();
            ResultSet rset = dbmd.getTables(null, null, "EXTERNALTRANSPORTSR", null);

            if (rset.next() && rset.getString(3).equals("EXTERNALTRANSPORTSR")){
                // table exists
            } else {
                // Create table
                Statement stmt = conn.createStatement();
                stmt.execute("CREATE TABLE EXTERNALTRANSPORTSR ( "
                        + "srID VARCHAR(50) , "
                        + "patientID VARCHAR(50), "
                        + "dropOffLocation VARCHAR(50), "
                        + "formOfTransport VARCHAR(50), "
                        + "PRIMARY KEY (srID),"
                        + "CONSTRAINT FK_EXTERNALTRANSPORTSR_MainSR FOREIGN KEY (srID) REFERENCES MainSR (srID) )"); //ON DELETE SET NULL
            }
        } catch (SQLException e) {
            System.out.println("Create EXTERNALTRANSPORTSR Table: Failed!");
            e.printStackTrace();
        }

    }

    @Override
    public void dropTable() {
        try {
            Statement stmt = conn.createStatement();
            stmt.execute("DROP TABLE EXTERNALTRANSPORTSR");
        } catch (SQLException e) {
            System.out.println("Drop EXTERNALTRANSPORTSR Table: Failed!");
        }

    }

    @Override
    public void restoreTable(List<ExternalTransportSR> list) {

        createTable();

        for (ExternalTransportSR externalTransportSR : list) {
            addValue(externalTransportSR);
        }

    }
}

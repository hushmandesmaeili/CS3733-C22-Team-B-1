package edu.wpi.cs3733.c22.teamB.oldEntity;

import java.util.List;

public interface IDatabase<T> {
    List<T> getAllNodes();

    T getNode(String nodeID);

    void deleteNode(String nodeID);

    void updateNode(T node);

    void insertNode(T node);

    void restore(List<T> list);

    void drop();

    // boolean isInTable(String nodeID);
}

package userpack;

import productionpack.Actor;
import productionpack.Production;
import observerpack.Request;

public interface StaffInterface {
    void addProductionSystem(Production p);
    void addActorSystem(Actor a);
    void removeProductionSystem(String name);
    void removeActorSystem(String name);
    void updateProduction(Production old_p, Production new_p);
    void updateActor(Actor old_a, Actor new_a);
    void solveRequest(Request request, Boolean accept);
}

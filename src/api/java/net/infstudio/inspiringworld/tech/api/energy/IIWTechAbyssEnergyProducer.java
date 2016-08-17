package net.infstudio.inspiringworld.tech.api.energy;

public interface IIWTechAbyssEnergyProducer extends IIWTechEnergyProducer {
    int getContainedAbyssEnergy();

    void setContainedAbyssEnergy(int energy);

    int extractAbyssEnergy(int maxExtract, boolean simulate);
}

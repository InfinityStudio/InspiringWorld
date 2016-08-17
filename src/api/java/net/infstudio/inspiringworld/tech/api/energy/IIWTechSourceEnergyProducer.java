package net.infstudio.inspiringworld.tech.api.energy;

public interface IIWTechSourceEnergyProducer extends IIWTechEnergyProducer {
    int getContainedSourceEnergy();

    void setContainedSourceEnergy(int energy);

    int extractSourceEnergy(int maxExtract, boolean simulate);
}

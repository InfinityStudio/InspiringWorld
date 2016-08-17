package net.infstudio.inspiringworld.tech.api.energy;

public interface IIWTechAbyssEnergyProvider extends IIWTechEnergyProducer {
    int extractAbyssEnergy(int maxExtract, boolean simulate);
}

package net.infstudio.inspiringworld.tech.api.energy;

public interface IIWTechSourceEnergyProvider extends IIWTechEnergyProducer {
    int extractSourceEnergy(int maxExtract, boolean simulate);
}

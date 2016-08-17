package net.infstudio.inspiringworld.tech.api.energy;

public interface IIWTechAbyssEnergyProvider extends IIWTechEnergyProvider {
    int extractAbyssEnergy(int maxExtract, boolean simulate);
}

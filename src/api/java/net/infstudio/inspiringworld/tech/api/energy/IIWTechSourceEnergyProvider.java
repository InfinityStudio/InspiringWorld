package net.infstudio.inspiringworld.tech.api.energy;

public interface IIWTechSourceEnergyProvider extends IIWTechEnergyProvider {
    int extractSourceEnergy(int maxExtract, boolean simulate);
}

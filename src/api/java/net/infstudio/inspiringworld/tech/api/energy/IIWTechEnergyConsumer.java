package net.infstudio.inspiringworld.tech.api.energy;

public interface IIWTechEnergyConsumer {
    int getEnergySpace();

    void setEnergySpace(int energy);

    int receiveEnergy(int maxReceive, boolean simulate);
}

package net.infstudio.inspiringworld.tech.api.energy;

public interface IIWTechEnergyConsumer {
    int receiveEnergy(int maxReceive, boolean simulate);
}

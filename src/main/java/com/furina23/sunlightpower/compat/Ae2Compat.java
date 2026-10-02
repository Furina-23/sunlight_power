package com.furina23.sunlightpower.compat;

import appeng.api.config.AccessRestriction;
import appeng.api.config.Actionable;
import appeng.api.config.PowerMultiplier;
import appeng.api.config.PowerUnits;
import appeng.api.implementations.items.IAEItemPowerStorage;
import appeng.api.networking.GridHelper;
import appeng.api.networking.IGridNode;
import appeng.api.networking.IGridNodeListener;
import appeng.api.networking.IInWorldGridNodeHost;
import appeng.api.networking.IManagedGridNode;
import appeng.api.networking.energy.IAEPowerStorage;
import appeng.api.util.AECableType;
import com.furina23.sunlightpower.SunlightPower;
import com.furina23.sunlightpower.block.entity.SolarPanelBlockEntity;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

import java.util.EnumSet;
import java.util.Map;
import java.util.WeakHashMap;

/** AE2 Fabric 15.4.11 官方网络 API 兼容层。 */
public final class Ae2Compat {
    private static final Map<SolarPanelBlockEntity, Ae2Host> HOSTS = new WeakHashMap<>();

    private Ae2Compat() {
    }

    public static void bootstrap() {
        IInWorldGridNodeHost.LOOKUP.registerForBlockEntity(
                (panel, ignored) -> HOSTS.computeIfAbsent(panel, Ae2Host::new), SunlightPower.SOLAR_PANEL_ENTITY);
        SunlightPower.LOGGER.info("已启用 AE2 Fabric 15.4.11 能源网络接口");
    }

    public static long transfer(SolarPanelBlockEntity panel, long amount) {
        return 0;
    }

    public static long chargeItem(ItemStack stack, long amount) {
        if (!(stack.getItem() instanceof IAEItemPowerStorage storage)) return 0;
        double offeredAe = PowerUnits.TR.convertTo(PowerUnits.AE, amount);
        double leftover = storage.injectAEPower(stack, offeredAe, Actionable.MODULATE);
        double acceptedAe = Math.max(0, offeredAe - leftover);
        return Math.max(0, Math.min(amount, (long) Math.floor(PowerUnits.AE.convertTo(PowerUnits.TR, acceptedAe))));
    }

    public static boolean canChargeItem(ItemStack stack) {
        return stack.getItem() instanceof IAEItemPowerStorage storage
                && storage.getPowerFlow(stack).isAllowInsertion();
    }

    public static void remove(SolarPanelBlockEntity panel) {
        Ae2Host host = HOSTS.remove(panel);
        if (host != null) host.destroy();
    }

    public static void save(SolarPanelBlockEntity panel, CompoundTag tag) {
        Ae2Host host = HOSTS.get(panel);
        if (host != null) host.save(tag);
    }

    public static void load(SolarPanelBlockEntity panel, CompoundTag tag) {
        Ae2Host host = HOSTS.get(panel);
        if (host != null) host.load(tag);
    }

    private static final class Ae2Host implements IInWorldGridNodeHost, IGridNodeListener<Ae2Host> {
        private final SolarPanelBlockEntity panel;
        private final IManagedGridNode managedNode;
        private final Ae2PowerStorage storage;

        private Ae2Host(SolarPanelBlockEntity panel) {
            this.panel = panel;
            this.storage = new Ae2PowerStorage(panel);
            this.managedNode = GridHelper.createManagedNode(this, this)
                    .setInWorldNode(true)
                    .setIdlePowerUsage(0)
                    .setExposedOnSides(EnumSet.complementOf(EnumSet.of(Direction.UP)))
                    .setVisualRepresentation(SunlightPower.SOLAR_PANEL)
                    .addService(IAEPowerStorage.class, storage);
            if (panel.getLevel() != null) {
                GridHelper.onFirstTick(panel, ignored -> managedNode.create(panel.getLevel(), panel.getBlockPos()));
            }
        }

        @Override
        public IGridNode getGridNode(Direction direction) {
            return direction == Direction.UP ? null : managedNode.getNode();
        }

        @Override
        public AECableType getCableConnectionType(Direction direction) {
            return direction == Direction.UP ? AECableType.NONE : AECableType.SMART;
        }

        @Override
        public void onSaveChanges(Ae2Host owner, IGridNode node) {
            panel.setChanged();
        }

        private void destroy() {
            managedNode.destroy();
        }

        private void save(CompoundTag tag) {
            CompoundTag nodeTag = new CompoundTag();
            managedNode.saveToNBT(nodeTag);
            tag.put("Ae2Node", nodeTag);
        }

        private void load(CompoundTag tag) {
            if (tag.contains("Ae2Node")) managedNode.loadFromNBT(tag.getCompound("Ae2Node"));
        }
    }

    private static final class Ae2PowerStorage implements IAEPowerStorage {
        private final SolarPanelBlockEntity panel;

        private Ae2PowerStorage(SolarPanelBlockEntity panel) {
            this.panel = panel;
        }

        @Override
        public double extractAEPower(double amount, Actionable mode, PowerMultiplier multiplier) {
            double requestedAe = Math.max(0, multiplier.multiply(amount));
            double availableAe = PowerUnits.TR.convertTo(PowerUnits.AE,
                    Math.min(panel.getEnergy(), SunlightPower.CONFIG.maxOutputPerSide));
            double extractedAe = Math.min(requestedAe, availableAe);
            if (mode == Actionable.MODULATE && extractedAe > 0) {
                long extractedFe = Math.max(0, Math.min(panel.getEnergy(), (long) Math.ceil(
                        PowerUnits.AE.convertTo(PowerUnits.TR, extractedAe))));
                panel.setEnergy(panel.getEnergy() - extractedFe);
                extractedAe = PowerUnits.TR.convertTo(PowerUnits.AE, extractedFe);
            }
            return multiplier.divide(extractedAe);
        }

        @Override public double injectAEPower(double amount, Actionable mode) { return amount; }
        @Override public double getAEMaxPower() { return PowerUnits.TR.convertTo(PowerUnits.AE, panel.getCapacity()); }
        @Override public double getAECurrentPower() { return PowerUnits.TR.convertTo(PowerUnits.AE, panel.getEnergy()); }
        @Override public boolean isAEPublicPowerStorage() { return true; }
        @Override public AccessRestriction getPowerFlow() { return AccessRestriction.READ; }
        @Override public int getPriority() { return 0; }
    }
}

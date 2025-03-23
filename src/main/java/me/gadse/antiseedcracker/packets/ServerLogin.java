package me.gadse.antiseedcracker.packets;

import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerJoinGame;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerInfo;
import me.gadse.antiseedcracker.AntiSeedCracker;

public class ServerLogin extends PacketListenerAbstract {

    private final AntiSeedCracker plugin;
    private final boolean warnedForOutdatedVersion = false;

    public ServerLogin(AntiSeedCracker plugin) {
        super(PacketListenerPriority.HIGHEST);
        this.plugin = plugin;
    }

    @Override
    public void onPacketSend(PacketSendEvent event) {
        if (event.getPacketType() != PacketType.Play.Server.JOIN_GAME) return;

        WrapperPlayServerJoinGame wrapper = new WrapperPlayServerJoinGame(event);
        wrapper.read();
        long hashedSeed = wrapper.getHashedSeed();

        long randHashedSeed = plugin.randomizeHashedSeed(hashedSeed);
        wrapper.setHashedSeed(randHashedSeed);

        wrapper.write();
        event.setByteBuf(wrapper.buffer);
    }

//    @Override
//    public void onPacketSending(PacketEvent event) {
//        PacketContainer packet = event.getPacket();
//        try {
//            int structureSize = packet.getStructures().size();
//            if (structureSize == 0) {
//                plugin.getLogger().warning(
//                        "Can not write hashed seed at login for player " + event.getPlayer().getName() + "."
//                );
//                return;
//            }
//            InternalStructure structureModifier = packet.getStructures().read(structureSize - 1);
//            structureModifier.getLongs().write(
//                    0, plugin.randomizeHashedSeed(structureModifier.getLongs().read(0))
//            );
//        } catch (FieldAccessException | NullPointerException ex) {
//            // FieldAccessException is caused by old versions of Minecraft
//            // NPE is caused by old versions of ProtocolLib
//
//            packet.getLongs().write(0, plugin.randomizeHashedSeed(packet.getLongs().read(0)));
//        }
//
//        event.setPacket(packet);
//    }
}

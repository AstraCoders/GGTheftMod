package rok.theft.platform.neoforge;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import rok.theft.Theft;

/*
 * @author Lucasmellof, Lucas de Mello Freitas created on 05/10/2026
 */
record StartTheftPayload(int time) implements CustomPacketPayload {
    static final Type<StartTheftPayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Theft.MOD_ID, "start_theft"));
    static final StreamCodec<RegistryFriendlyByteBuf, StartTheftPayload> STREAM_CODEC =
            StreamCodec.composite(ByteBufCodecs.INT, StartTheftPayload::time, StartTheftPayload::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}

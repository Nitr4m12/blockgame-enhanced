package dev.jb0s.blockgameenhanced.gamefeature.mmovendor;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public enum MMOVendor {
    WARRIOR("Willy", "blacksmith", "76a6184e-0c98-206a-bdcc-152f8204a0cc"),
    BLACKSMITH("Markus", "blacksmith", "8ce2fee5-7dcb-2dbf-8baa-9414bc4b4d89"),
    GUARDIAN("Armand", "blacksmith", "7c7fdc09-6859-2e36-96f2-fbd3c6a53d3b"),
    SPELLCRAFTER("Sally", "spellhouse", "f5637962-c532-20ba-88fb-4ad598f176cf"),
    ALCHEMIST("Nina", "spellhouse", "1c4862d4-f6b8-2ae6-bb36-54d2d3c33de7"),
    SILK_WEAVER("Wendy", "spellhouse", "a054310e-b283-2551-9b3f-272dbbf9e577"),
    LUMBERJACK("Larry", "woodshack", "ee7ac063-aae3-2389-bfea-a88f01683d42"),
    PAUL("Paul", "woodshack", "52193204-6ca7-2529-b42b-f9238d92b95a"),
    MINER("Steve", "mining", "41333d8f-8738-2489-ba6f-c8c2942bd72f"),
    GEORGE("George", "mining", "2ce2e4bc-1d80-2b50-8b53-96e250a4d384"),
    HUNTER("Hanzo", "einherjar", "73ccd9dc-fb78-2974-8bdb-e384dece9bad"),
    LEATHERWORKER("Seymour", "leathershack", "d41d6086-3afb-26f3-8fcd-a433261c4f5a"),
    BOWYER("Brent", "leathershack", "230b7997-de6d-28c7-93cf-c9e71325939a"),
    FORGER("Forge", "einherjar", "e66ba72e-016f-2b42-9755-0456014d25a4"),
    SMITHY("Smith", "einherjar", "575effb5-e9ad-29ac-8aaa-eef752b656cc"),
    REPAIR("RepairBot 3000", "repairbot", ""),
    GOLEM("Metal Golem", "einherjar", "38340b78-c3e6-2e73-b6eb-182204f77134"),
    FISHERMAN("Franky", "fishing", "6ff4b517-410f-249f-b823-db36f4c8323a"),
    BAELIN("Baelin", "fishing", "47c1d7f0-735a-209a-ad40-ba07df665b27"),
    ARTIFICER("Eitri", "runecrafter", "ea167d76-599c-2137-8e25-f1a5faef0f16"),
    RUNE_CARVER("Brokkr", "runecrafter", "b516fbd9-9713-225f-b35f-1b57c71d6f2d"),
    STONEBEARD("Stonebeard", "runecrafter", "ee3d7904-54b8-2495-b7f2-f8469be42710"),
    RUNEHILDA("Runehilda", "runecrafter", "f61e381f-0346-266a-ba8e-63ee9b8f7781"),
    THAUMATURGE("Gregory", "healhouse", "0ced42aa-1eaa-2797-9d57-73ae84df3b48"),
    POTION_SELLER("Justin", "healhouse", "caf1d5ad-c732-2013-8955-1a0a793c9bd8"),
    WOOL_WEAVER("Porfirio", "healhouse", "9aa45b77-d27f-2201-9642-3a71029d5e09"),
    CHEF_KEN("Ken", "restaurant", "3fda0197-a9c9-28b3-9948-c18abcb65683"),
    CHEF_SUE("Chef Sue", "restaurant", "e22209e2-a2b2-28e8-bfa4-49dc49f00583"),
    CHEF_HOLIDAY("Chef Holiday", "restaurant", "7abfbad4-476f-2d86-b0a8-1b40ad700ee4"),
    JAM_MASTER("Jam Master", "restaurant", "b220454a-dbc9-28dc-b75e-4bb47bef0e02"),
    TEA_MASTER("Piggly Wiggly", "restaurant", "ade4b78b-fa12-224f-85d4-98071acf8dd0"),
    BOTANIST("Hesha", "botanist", "16c4f49e-15a1-2638-8999-de1bc14f0f5a"),
    MINT("Mint", "botanist", "11f6ec35-2d86-260c-b392-12a6f117a75e"),
    ARCHAEOLOGIST("Indy", "archaeology", "53a3283d-caeb-2348-963d-adbf3000e209"),
    MORTIMER("Mortimer", "archaeology", "71e2ca6f-4920-2126-bee5-7c6f0ed0152d"),
    BARON_WARBUCKS("Baron Warbucks", "einherjar", "aaafb875-043e-241b-aff2-ade03a2dd430"),
    SOOIE_CASA("Sooie", "casa", "01b4541b-f819-29ad-a488-6cad420643d3"),
    MAYOR_MCCHEESE("Mayor McCheese", "mayor", "4615d5b9-f9c7-26cc-8c8d-276632253c89");

    @Getter
    @Setter
    private String characterName;

    @Getter
    @Setter
    private String ui;

    @Getter
    @Setter
    private String uuid;

    MMOVendor(String name, String ui, String uuid) {
        setCharacterName(name);
        setUi(ui);
        setUuid(uuid);
    }

    /**
     * Determine a MMOVendor type by their character name.
     * @param name Name of the vendor's character.
     * @return Vendor matching provided name, null if not found.
     */
    public static MMOVendor getByName(String name) {
        for (MMOVendor x : MMOVendor.values()) {
            if(x.getCharacterName().equals(name)) {
                return x;
            }
        }

        return null;
    }

    public static MMOVendor getByUUID(String uuid) {
        for (MMOVendor vendor : MMOVendor.values()) {
            if (vendor.toString().equals(uuid))
                return vendor;
        }
        return null;
    }

    /**
     * Find the PlayerEntity associated with this vendor type.
     * @return PlayerEntity that is a vendor of this type
     */
    public PlayerEntity getVendorEntity() {
        MinecraftClient minecraft = MinecraftClient.getInstance();
        World world = minecraft.world; if(world == null) return null;

        for (PlayerEntity pe : world.getPlayers()) {
            if(pe.getUuidAsString().equals(uuid)) {
                return pe;
            }
        }

        return null;
    }
}

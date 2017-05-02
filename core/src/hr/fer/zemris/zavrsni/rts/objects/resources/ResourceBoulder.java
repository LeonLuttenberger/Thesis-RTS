package hr.fer.zemris.zavrsni.rts.objects.resources;

import hr.fer.zemris.zavrsni.rts.assets.Assets;

public class ResourceBoulder extends Resource {

    public ResourceBoulder() {
        super(Assets.getInstance().getResources().rock, 0);
    }
}

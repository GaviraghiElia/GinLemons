package it.unimib.ginlemons.ui.ricette;

import java.util.Comparator;

public class RicettaHelper
{
    private String id;
    private String name;
    private String type;

    public RicettaHelper() {}

    public RicettaHelper(String id, String name, String type)
    {
        this.id = id;
        this.name = name;
        this.type = type;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    // Ordine alfabetico crescente
    public static Comparator<RicettaHelper> OrdinaRicetteAlfabeticoAZ = new Comparator<RicettaHelper>()
    {
        @Override
        public int compare(RicettaHelper r1, RicettaHelper r2) {
            return r1.getName().compareToIgnoreCase(r2.getName());
        }
    };

    // Ordine alfabetico decrescente
    public static Comparator<RicettaHelper> OrdinaRicetteAlfabeticoZA = new Comparator<RicettaHelper>()
    {
        @Override
        public int compare(RicettaHelper r1, RicettaHelper r2) {
            return r2.getName().compareToIgnoreCase(r1.getName());
        }
    };
}

package edu.pitt.rods.apollo.statetransitionnetwork.braithwaite;

public class NodeGroupConsts {

    public static final String WILL_TAKE_PROPHYLAXIS_BEFORE_CERTAIN = "needs_prophylaxis_now";
    public static final String WILL_TAKE_PROPHYLAXIS_AFTER_CERTAIN = "needs_prophylaxis_when_certain";

    public static final String PRODROMAL_TREATMENT = "prodromal treatment";

    public static final String TREATMENT = "treatment";
    public static final String TREATABLE_DISEASE_PROGRESSION = "treatable_disease_progression";

    public static final String REFRACTORY_TRANSITION = "refractory_transition";

    public static final String DISEASE_PROGRESSION_FOR_MULTI_BIN_PER_HOUR_MODEL = "disease_progression_mbph";
    public static final String DISEASE_PROGRESSION_FOR_SEIR_MODEL = "disease_progression_seir";
    public static final String TO_PROD_PROGRESSION = "disease_progression_prod";
    public static final String TO_FUL_PROGRESSION = "disease_progression_ful";
    public static final String TO_DEAD_PROGRESSION = "disease_progression_dead";
    public static final String TO_RECOVER_FROM_ASX_PROGRESSION = "disease_progression_asxR";
    public static final String TO_RECOVER_FROM_PROD_PROGRESSION = "disease_progression_prodR";
    public static final String TO_RECOVER_FROM_FUL_PROGRESSION = "disease_progression_deadR";

    public static final String SEIR_TO_INFECTED = "to_infected";
    public static final String SEIR_TO_INFECTIOUS = "to_infectious";
    public static final String SEIR_TO_IMMUNE = "to_immune";
    public static final String SEIR_TO_SUSCEPTIBLE_VACCINATED = "to_susceptible_vaccinated";
    
    public static final String SEIR_C_SUSCEPTIBLE = "count_susceptible";
    public static final String SEIR_C_EXPOSED = "count_exposed";
    public static final String SEIR_C_INFECTED = "count_infected";
    public static final String SEIR__C_RECOVERED = "count_recovered";
    public static final String SEIR_C_SUSCEPTIBLE_VACCINATED = "count_susceptible_vaccinated";
}

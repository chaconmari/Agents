# Agents
Different Agents implemented:

-MaxMin Payoff:  This agent plays the strategy that maximizes the minimum possible payoff against any possible opponent action. 

-MinMax Regret:  This agent plays the strategy that minimizes the maximum possible regret for any possible opponent action.

-Pure Strategy Nash Equilibrium:  This agent plays according to a pure-strategy Nash equilibrium (PSNE) of the game if one exists.  If there are multiple PSNE, chooses the one with the maximum payoff for the player you are choosing a strategy for.  If there are no PSNE, plays the profile that has the smallest benefit to deviating for any player (i.e., the closest approximate Nash equilibrium.  

(For the following agents, there are two important complications.  First, the agent may have uncertainty about the actual payoffs in the game. Second, you may be playing the same opponent repeatedly in the same game, with gives you the opportunity to learn about and respond to their strategy.)

-One-shot games with no uncertainty

-One-shot games with uncertainty

-Repeated games with uncertainty


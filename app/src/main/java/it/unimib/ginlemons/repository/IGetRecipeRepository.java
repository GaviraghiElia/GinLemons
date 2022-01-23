package it.unimib.ginlemons.repository;

import it.unimib.ginlemons.utils.Ricetta;

public interface IGetRecipeRepository
{
    Ricetta getRecipeById(String id);
}

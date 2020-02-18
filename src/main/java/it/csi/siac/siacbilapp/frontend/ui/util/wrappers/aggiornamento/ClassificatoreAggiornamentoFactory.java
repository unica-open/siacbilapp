/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.util.wrappers.aggiornamento;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siacbilapp.frontend.ui.model.capentgest.AggiornaCapitoloEntrataGestioneModel;
import it.csi.siac.siacbilapp.frontend.ui.model.capentgest.AggiornaMassivaCapitoloEntrataGestioneModel;
import it.csi.siac.siacbilapp.frontend.ui.model.capentprev.AggiornaCapitoloEntrataPrevisioneModel;
import it.csi.siac.siacbilapp.frontend.ui.model.capentprev.AggiornaMassivaCapitoloEntrataPrevisioneModel;
import it.csi.siac.siacbilapp.frontend.ui.model.capuscgest.AggiornaCapitoloUscitaGestioneModel;
import it.csi.siac.siacbilapp.frontend.ui.model.capuscgest.AggiornaMassivaCapitoloUscitaGestioneModel;
import it.csi.siac.siacbilapp.frontend.ui.model.capuscprev.AggiornaCapitoloUscitaPrevisioneModel;
import it.csi.siac.siacbilapp.frontend.ui.model.capuscprev.AggiornaMassivaCapitoloUscitaPrevisioneModel;
import it.csi.siac.siacbilapp.frontend.ui.model.commons.CapitoloModel;
import it.csi.siac.siacbilapp.frontend.ui.util.ReflectionUtil;
import it.csi.siac.siacbilser.model.ElementoPianoDeiConti;
import it.csi.siac.siacbilser.model.TipoFinanziamento;
import it.csi.siac.siacbilser.model.TipoFondo;
import it.csi.siac.siaccorser.model.Codifica;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;

/**
 * Factory per il wrapping dei classificatori per l'aggiornamento.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 27/08/2013
 *
 */
public final class ClassificatoreAggiornamentoFactory {
	
	/** Numero dei classificatori generici presenti */
	private static final int NUMERO_CLASSIFICATORI_GENERICI = 15;
	
	/** Lista dei classificatori di base da injettare nel {@link ClassificatoreAggiornamento} */
	@SuppressWarnings("unchecked")
	private static final List<Class<? extends Codifica>> CLASSIFICATORI_BASE = Arrays.asList(ElementoPianoDeiConti.class, 
			StrutturaAmministrativoContabile.class, TipoFinanziamento.class, TipoFondo.class);
	
	/** Non permettere l'instanziazione della classe */
	private ClassificatoreAggiornamentoFactory() {
	}
	
	/* **** Puntuali **** */
	
	/**
	 * Wrapper per l'aggiornamento del Capitolo di Uscita Previsione.
	 *  
	 * @param model il model da wrappare
	 * 
	 * @return il wrapper
	 */
	public static ClassificatoreAggiornamentoCapitoloUscita getInstance(AggiornaCapitoloUscitaPrevisioneModel model) {
		// Fallback in caso di null
		if(model == null) {
			return null;
		}
				
		ClassificatoreAggiornamentoCapitoloUscita wrapper = new ClassificatoreAggiornamentoCapitoloUscita();
		
		// Popolamento dei campi comuni
		popolaCampiComuni(wrapper, model);
		
		// Popolamento dei campi specifici
		wrapper.setMissione(model.getMissione());
		wrapper.setProgramma(model.getProgramma());
		wrapper.setTitoloSpesa(model.getTitoloSpesa());
		wrapper.setMacroaggregato(model.getMacroaggregato());
		wrapper.setClassificazioneCofog(model.getClassificazioneCofog());
		wrapper.setSiopeSpesa(model.getSiopeSpesa());
		wrapper.setRicorrenteSpesa(model.getRicorrenteSpesa());
		wrapper.setPerimetroSanitarioSpesa(model.getPerimetroSanitarioSpesa());
		wrapper.setTransazioneUnioneEuropeaSpesa(model.getTransazioneUnioneEuropeaSpesa());
		wrapper.setPoliticheRegionaliUnitarie(model.getPoliticheRegionaliUnitarie());
		
		return wrapper;
	}
	
	/**
	 * Wrapper per l'aggiornamento del Capitolo di Uscita Gestione.
	 *  
	 * @param model il model da wrappare
	 * 
	 * @return il wrapper
	 */
	public static ClassificatoreAggiornamentoCapitoloUscita getInstance(AggiornaCapitoloUscitaGestioneModel model) {
		// Fallback in caso di null
		if(model == null) {
			return null;
		}
		
		ClassificatoreAggiornamentoCapitoloUscita wrapper = new ClassificatoreAggiornamentoCapitoloUscita();
		
		// Popolamento dei campi comuni
		popolaCampiComuni(wrapper, model);
		
		// Popolamento dei campi specifici
		wrapper.setMissione(model.getMissione());
		wrapper.setProgramma(model.getProgramma());
		wrapper.setTitoloSpesa(model.getTitoloSpesa());
		wrapper.setMacroaggregato(model.getMacroaggregato());
		wrapper.setClassificazioneCofog(model.getClassificazioneCofog());
		wrapper.setSiopeSpesa(model.getSiopeSpesa());
		wrapper.setRicorrenteSpesa(model.getRicorrenteSpesa());
		wrapper.setPerimetroSanitarioSpesa(model.getPerimetroSanitarioSpesa());
		wrapper.setTransazioneUnioneEuropeaSpesa(model.getTransazioneUnioneEuropeaSpesa());
		wrapper.setPoliticheRegionaliUnitarie(model.getPoliticheRegionaliUnitarie());
		return wrapper;
	}
	
	/**
	 * Wrapper per l'aggiornamento del Capitolo di Entrata Previsione.
	 *  
	 * @param model il model da wrappare
	 * 
	 * @return il wrapper
	 */
	public static ClassificatoreAggiornamentoCapitoloEntrata getInstance(AggiornaCapitoloEntrataPrevisioneModel model) {
		// Fallback in caso di null
		if(model == null) {
			return null;
		}
				
		ClassificatoreAggiornamentoCapitoloEntrata wrapper = new ClassificatoreAggiornamentoCapitoloEntrata();
		
		// Popolamento dei campi comuni
		popolaCampiComuni(wrapper, model);
		
		// Popolamento dei campi specifici
		wrapper.setTitoloEntrata(model.getTitoloEntrata());
		wrapper.setTipologiaTitolo(model.getTipologiaTitolo());
		wrapper.setCategoriaTipologiaTitolo(model.getCategoriaTipologiaTitolo());
		wrapper.setSiopeEntrata(model.getSiopeEntrata());
		wrapper.setRicorrenteEntrata(model.getRicorrenteEntrata());
		wrapper.setPerimetroSanitarioEntrata(model.getPerimetroSanitarioEntrata());
		wrapper.setTransazioneUnioneEuropeaEntrata(model.getTransazioneUnioneEuropeaEntrata());
		return wrapper;
	}
	
	/**
	 * Wrapper per l'aggiornamento del Capitolo di Entrata Gestione.
	 *  
	 * @param model il model da wrappare
	 * 
	 * @return il wrapper
	 */
	public static ClassificatoreAggiornamentoCapitoloEntrata getInstance(AggiornaCapitoloEntrataGestioneModel model) {
		// Fallback in caso di null
		if(model == null) {
			return null;
		}
				
		ClassificatoreAggiornamentoCapitoloEntrata wrapper = new ClassificatoreAggiornamentoCapitoloEntrata();
		
		// Popolamento dei campi comuni
		popolaCampiComuni(wrapper, model);
		
		// Popolamento dei campi specifici
		wrapper.setTitoloEntrata(model.getTitoloEntrata());
		wrapper.setTipologiaTitolo(model.getTipologiaTitolo());
		wrapper.setCategoriaTipologiaTitolo(model.getCategoriaTipologiaTitolo());
		wrapper.setSiopeEntrata(model.getSiopeEntrata());
		wrapper.setRicorrenteEntrata(model.getRicorrenteEntrata());
		wrapper.setPerimetroSanitarioEntrata(model.getPerimetroSanitarioEntrata());
		wrapper.setTransazioneUnioneEuropeaEntrata(model.getTransazioneUnioneEuropeaEntrata());
		return wrapper;
	}
	
	/* **** Massivi **** */
	
	/**
	 * Wrapper per l'aggiornamento del Capitolo di Uscita Previsione.
	 *  
	 * @param model il model da wrappare
	 * 
	 * @return il wrapper
	 */
	public static ClassificatoreAggiornamentoCapitoloUscita getInstance(AggiornaMassivaCapitoloUscitaPrevisioneModel model) {
		// Fallback in caso di null
		if(model == null) {
			return null;
		}
				
		ClassificatoreAggiornamentoCapitoloUscita wrapper = new ClassificatoreAggiornamentoCapitoloUscita();
		
		// Popolamento dei campi comuni
		popolaCampiComuni(wrapper, model);
		
		// Popolamento dei campi specifici
		wrapper.setProgramma(model.getProgramma());
		wrapper.setMacroaggregato(model.getMacroaggregato());
		wrapper.setClassificazioneCofog(model.getClassificazioneCofog());
		wrapper.setSiopeSpesa(model.getSiopeSpesa());
		wrapper.setRicorrenteSpesa(model.getRicorrenteSpesa());
		wrapper.setPerimetroSanitarioSpesa(model.getPerimetroSanitarioSpesa());
		wrapper.setTransazioneUnioneEuropeaSpesa(model.getTransazioneUnioneEuropeaSpesa());
		wrapper.setPoliticheRegionaliUnitarie(model.getPoliticheRegionaliUnitarie());
		
		return wrapper;
	}
	
	/**
	 * Wrapper per l'aggiornamento del Capitolo di Uscita Gestione.
	 *  
	 * @param model il model da wrappare
	 * 
	 * @return il wrapper
	 */
	public static ClassificatoreAggiornamentoCapitoloUscita getInstance(AggiornaMassivaCapitoloUscitaGestioneModel model) {
		// Fallback in caso di null
		if(model == null) {
			return null;
		}
		
		ClassificatoreAggiornamentoCapitoloUscita wrapper = new ClassificatoreAggiornamentoCapitoloUscita();
		
		// Popolamento dei campi comuni
		popolaCampiComuni(wrapper, model);
		
		// Popolamento dei campi specifici
		wrapper.setProgramma(model.getProgramma());
		wrapper.setMacroaggregato(model.getMacroaggregato());
		wrapper.setClassificazioneCofog(model.getClassificazioneCofog());
		wrapper.setSiopeSpesa(model.getSiopeSpesa());
		wrapper.setRicorrenteSpesa(model.getRicorrenteSpesa());
		wrapper.setPerimetroSanitarioSpesa(model.getPerimetroSanitarioSpesa());
		wrapper.setTransazioneUnioneEuropeaSpesa(model.getTransazioneUnioneEuropeaSpesa());
		wrapper.setPoliticheRegionaliUnitarie(model.getPoliticheRegionaliUnitarie());
		return wrapper;
	}
	
	/**
	 * Wrapper per l'aggiornamento del Capitolo di Entrata Previsione.
	 *  
	 * @param model il model da wrappare
	 * 
	 * @return il wrapper
	 */
	public static ClassificatoreAggiornamentoCapitoloEntrata getInstance(AggiornaMassivaCapitoloEntrataPrevisioneModel model) {
		// Fallback in caso di null
		if(model == null) {
			return null;
		}
				
		ClassificatoreAggiornamentoCapitoloEntrata wrapper = new ClassificatoreAggiornamentoCapitoloEntrata();
		
		// Popolamento dei campi comuni
		popolaCampiComuni(wrapper, model);
		
		// Popolamento dei campi specifici
		wrapper.setCategoriaTipologiaTitolo(model.getCategoriaTipologiaTitolo());
		wrapper.setSiopeEntrata(model.getSiopeEntrata());
		wrapper.setRicorrenteEntrata(model.getRicorrenteEntrata());
		wrapper.setPerimetroSanitarioEntrata(model.getPerimetroSanitarioEntrata());
		wrapper.setTransazioneUnioneEuropeaEntrata(model.getTransazioneUnioneEuropeaEntrata());
		return wrapper;
	}
	
	/**
	 * Wrapper per l'aggiornamento del Capitolo di Entrata Gestione.
	 *  
	 * @param model il model da wrappare
	 * 
	 * @return il wrapper
	 */
	public static ClassificatoreAggiornamentoCapitoloEntrata getInstance(AggiornaMassivaCapitoloEntrataGestioneModel model) {
		// Fallback in caso di null
		if(model == null) {
			return null;
		}
				
		ClassificatoreAggiornamentoCapitoloEntrata wrapper = new ClassificatoreAggiornamentoCapitoloEntrata();
		
		// Popolamento dei campi comuni
		popolaCampiComuni(wrapper, model);
		
		// Popolamento dei campi specifici
		wrapper.setCategoriaTipologiaTitolo(model.getCategoriaTipologiaTitolo());
		wrapper.setSiopeEntrata(model.getSiopeEntrata());
		wrapper.setRicorrenteEntrata(model.getRicorrenteEntrata());
		wrapper.setPerimetroSanitarioEntrata(model.getPerimetroSanitarioEntrata());
		wrapper.setTransazioneUnioneEuropeaEntrata(model.getTransazioneUnioneEuropeaEntrata());
		return wrapper;
	}
	
	/**
	 * Metodo di utilit&agrave; per la creazione di un {@link ClassificatoreAggiornamento} a partire dal model.
	 * @param <T> la tipizzazione del classificatore aggiornamento
	 * @param model il model di riferimento
	 * 
	 * @return il wrapper creato
	 */
	@SuppressWarnings("unchecked")
	public static <T extends ClassificatoreAggiornamento> T getInstance(GenericBilancioModel model) {
		T response = null;
		if(model instanceof AggiornaCapitoloUscitaPrevisioneModel) {
			response = (T)getInstance((AggiornaCapitoloUscitaPrevisioneModel)model);
		} else if (model instanceof AggiornaCapitoloUscitaGestioneModel) {
			response = (T)getInstance((AggiornaCapitoloUscitaGestioneModel)model);
		} else if (model instanceof AggiornaCapitoloEntrataPrevisioneModel) {
			response = (T)getInstance((AggiornaCapitoloEntrataPrevisioneModel)model);
		} else if (model instanceof AggiornaCapitoloEntrataGestioneModel) {
			response = (T)getInstance((AggiornaCapitoloEntrataGestioneModel)model);
		} else if(model instanceof AggiornaMassivaCapitoloUscitaPrevisioneModel) {
			response = (T)getInstance((AggiornaMassivaCapitoloUscitaPrevisioneModel)model);
		} else if (model instanceof AggiornaMassivaCapitoloUscitaGestioneModel) {
			response = (T)getInstance((AggiornaMassivaCapitoloUscitaGestioneModel)model);
		} else if (model instanceof AggiornaMassivaCapitoloEntrataPrevisioneModel) {
			response = (T)getInstance((AggiornaMassivaCapitoloEntrataPrevisioneModel)model);
		} else if (model instanceof AggiornaMassivaCapitoloEntrataGestioneModel) {
			response = (T)getInstance((AggiornaMassivaCapitoloEntrataGestioneModel)model);
		} else {
			throw new UnsupportedOperationException("Il model fornito non supporta la conversione");
		}
		return response;
	}
	
	/**
	 * Metodo di utilit&agrave; per la creazione di un {@link ClassificatoreAggiornamento} a partire dal model e dalla lista
	 * dei classificatori presenti.
	 *
	 * @param classificatoreAggiornamento il classificatore di aggiornamento
	 * @param model                       il model di riferimento
	 * 
	 * @return il wrapper creato
	 */
	private static ClassificatoreAggiornamento popolaCampiComuni(ClassificatoreAggiornamento classificatoreAggiornamento, 
			CapitoloModel model) {
		
		/* Classificatori di base */
		for(Class<? extends Codifica> classificatore : CLASSIFICATORI_BASE) {
			String nomeCampo = StringUtils.uncapitalize(classificatore.getSimpleName());
			ReflectionUtil.copyField(model, classificatoreAggiornamento, nomeCampo);
		}
		/* Classificatori generici */
		for(int i = 0; i < NUMERO_CLASSIFICATORI_GENERICI; i++) {
			String nomeCampo = "classificatoreGenerico" + (i + 1);
			ReflectionUtil.copyField(model, classificatoreAggiornamento, nomeCampo);
		}
		
		return classificatoreAggiornamento;
	}

}

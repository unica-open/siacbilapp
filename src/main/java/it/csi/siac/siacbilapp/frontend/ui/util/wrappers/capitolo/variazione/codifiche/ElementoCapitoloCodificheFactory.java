/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.variazione.codifiche;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import it.csi.siac.siacbilapp.frontend.ui.util.ReflectionUtil;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.model.CategoriaTipologiaTitolo;
import it.csi.siac.siacbilser.model.ClassificazioneCofog;
import it.csi.siac.siacbilser.model.DettaglioVariazioneCodificaCapitolo;
import it.csi.siac.siacbilser.model.ElementoPianoDeiConti;
import it.csi.siac.siacbilser.model.Macroaggregato;
import it.csi.siac.siacbilser.model.Missione;
import it.csi.siac.siacbilser.model.PerimetroSanitarioEntrata;
import it.csi.siac.siacbilser.model.PerimetroSanitarioSpesa;
import it.csi.siac.siacbilser.model.PoliticheRegionaliUnitarie;
import it.csi.siac.siacbilser.model.Programma;
import it.csi.siac.siacbilser.model.RicorrenteEntrata;
import it.csi.siac.siacbilser.model.RicorrenteSpesa;
import it.csi.siac.siacbilser.model.SiopeEntrata;
import it.csi.siac.siacbilser.model.SiopeSpesa;
import it.csi.siac.siacbilser.model.TipoCapitolo;
import it.csi.siac.siacbilser.model.TipoFinanziamento;
import it.csi.siac.siacbilser.model.TipoFondo;
import it.csi.siac.siacbilser.model.TipologiaTitolo;
import it.csi.siac.siacbilser.model.TitoloEntrata;
import it.csi.siac.siacbilser.model.TitoloSpesa;
import it.csi.siac.siacbilser.model.TransazioneUnioneEuropeaEntrata;
import it.csi.siac.siacbilser.model.TransazioneUnioneEuropeaSpesa;
import it.csi.siac.siaccommon.util.log.LogUtil;
import it.csi.siac.siaccorser.model.ClassificatoreGenerico;
import it.csi.siac.siaccorser.model.ClassificatoreGerarchico;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siaccorser.model.TipoClassificatore;


/**
 * Classe di factory per l'ElementoCapitoloCodifiche.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 25/10/2013
 *
 */
public final class ElementoCapitoloCodificheFactory {
	
	private static final LogUtil LOG = new LogUtil(ElementoCapitoloCodificheFactory.class);
	
	/** Non permettere l'instanziazione della classe */
	private ElementoCapitoloCodificheFactory() {
	}
	
	/**
	 * Ottiene un'istanza a partire dal capitolo.
	 * 
	 * @param capitolo il capitolo da wrappare
	 * 
	 * @return il wrapper ottenuto
	 */
	public static ElementoCapitoloCodifiche getInstance(Capitolo<?, ?> capitolo) {
		ElementoCapitoloCodifiche result = new ElementoCapitoloCodifiche();
		popolaCampi(result, capitolo);
		return result;
	}
	
	/**
	 * Ottiene un'istanza a partire dal dettaglio della variazione.
	 * 
	 * @param dettaglio il dettaglio da wrappare
	 * 
	 * @return il wrapper ottenuto
	 */
	public static ElementoCapitoloCodifiche getInstance(DettaglioVariazioneCodificaCapitolo dettaglio) {
		ElementoCapitoloCodifiche result = getInstance(dettaglio.getCapitolo());
		for(ClassificatoreGerarchico c : dettaglio.getClassificatoriGerarchici()) {
			result.addClassificatoreGerarchico(c);
		}
		for(ClassificatoreGenerico c : dettaglio.getClassificatoriGenerici()) {
			result.addClassificatoreGenerico(c);
		}
		result.impostaDenominazioneCapitolo();
		result.impostaDescrizioneCodifiche();
		return result;
	}
	
	/**
	 * Popola i campi del wrapper.
	 * 
	 * @param result   il wrapper ad popolare
	 * @param capitolo il capitolo da cui popolare il wrapper
	 */
	private static void popolaCampi(ElementoCapitoloCodifiche result, Capitolo<?, ?> capitolo) {
		result.setUid(capitolo.getUid());
		result.setDescrizioneCapitolo(capitolo.getDescrizione());
		result.setDescrizioneArticolo(capitolo.getDescrizioneArticolo());
		result.setAnnoCapitolo(capitolo.getAnnoCapitolo());
		result.setNumeroCapitolo(capitolo.getNumeroCapitolo());
		result.setNumeroArticolo(capitolo.getNumeroArticolo());
		result.setNumeroUEB(capitolo.getNumeroUEB());
		result.setNote(capitolo.getNote());
		// La categoria
		result.setCategoriaCapitolo(capitolo.getCategoriaCapitolo());
		
		TipoCapitolo tipoCapitolo = capitolo.getTipoCapitolo();
		result.setTipoCapitolo(tipoCapitolo);
		boolean capitoloDiUscita = TipoCapitolo.CAPITOLO_USCITA_PREVISIONE.equals(tipoCapitolo) || TipoCapitolo.CAPITOLO_USCITA_GESTIONE.equals(tipoCapitolo);
		
		// Aggiungo l'uid alla lista
		result.getListaUidCapitolo().add(capitolo.getUid());
		
		popolaClassificatoriComuni(result, capitolo);
		popolaFlagsComuni(result, capitolo);
		
		if(capitoloDiUscita) {
			popolaClassificatoriUscita(result, capitolo);
			popolaFlagsUscita(result, capitolo);
		} else {
			popolaClassificatoriEntrata(result, capitolo);
		}
		
		result.impostaDenominazioneCapitolo();
	}
	
	/**
	 * Imposta i classificatori per l'uscita.
	 * 
	 * @param result   il wrapper da popolare
	 * @param capitolo il capitolo da wrappare
	 */
	private static void popolaClassificatoriUscita(ElementoCapitoloCodifiche result, Capitolo<?, ?> capitolo) {
		impostaCampo(result, capitolo, Missione.class);
		impostaCampo(result, capitolo, Programma.class);
		impostaCampo(result, capitolo, ClassificazioneCofog.class);
		impostaCampo(result, capitolo, TitoloSpesa.class);
		impostaCampo(result, capitolo, Macroaggregato.class);
		impostaCampo(result, capitolo, SiopeSpesa.class);
		impostaCampo(result, capitolo, RicorrenteSpesa.class);
		impostaCampo(result, capitolo, PerimetroSanitarioSpesa.class);
		impostaCampo(result, capitolo, TransazioneUnioneEuropeaSpesa.class);
		impostaCampo(result, capitolo, PoliticheRegionaliUnitarie.class);
	}
	
	/**
	 * Imposta i classificatori per l'entrata.
	 * 
	 * @param result   il wrapper da popolare
	 * @param capitolo il capitolo da wrappare
	 */
	private static void popolaClassificatoriEntrata(ElementoCapitoloCodifiche result, Capitolo<?, ?> capitolo) {
		impostaCampo(result, capitolo, TitoloEntrata.class);
		impostaCampo(result, capitolo, TipologiaTitolo.class);
		impostaCampo(result, capitolo, CategoriaTipologiaTitolo.class);
		impostaCampo(result, capitolo, SiopeEntrata.class);
		impostaCampo(result, capitolo, RicorrenteEntrata.class);
		impostaCampo(result, capitolo, PerimetroSanitarioEntrata.class);
		impostaCampo(result, capitolo, TransazioneUnioneEuropeaEntrata.class);
	}
	
	/**
	 * Imposta i classificatori comuni.
	 * 
	 * @param result   il wrapper da popolare
	 * @param capitolo il capitolo da wrappare
	 */
	private static void popolaClassificatoriComuni(ElementoCapitoloCodifiche result, Capitolo<?, ?> capitolo) {
		impostaCampo(result, capitolo, ElementoPianoDeiConti.class);
		impostaCampo(result, capitolo, StrutturaAmministrativoContabile.class);
		impostaCampo(result, capitolo, TipoFondo.class);
		impostaCampo(result, capitolo, TipoFinanziamento.class);
		
		impostaClassificatoriGenerici(result, capitolo);
	}
	
	/**
	 * Popola i flag per il capitolo di uscita.
	 * 
	 * @param result   il wrapper da popolare
	 * @param capitolo il capitolo da wrappare
	 */
	private static void popolaFlagsUscita(ElementoCapitoloCodifiche result, Capitolo<?, ?> capitolo) {
		impostaFlag(result, capitolo, "flagFondoPluriennaleVinc", "flagFondoPluriennaleVincolato");
		impostaFlag(result, capitolo, "flagFondoSvalutazioneCred", "flagFondoSvalutazioneCrediti");
		impostaFlag(result, capitolo, "funzDelegateRegione", "flagFunzioniDelegateRegione");
	}
	
	/**
	 * Popola i flag comuni.
	 * 
	 * @param result   il wrapper da popolare
	 * @param capitolo il capitolo da wrappare
	 */
	private static void popolaFlagsComuni(ElementoCapitoloCodifiche result, Capitolo<?, ?> capitolo) {
		impostaFlag(result, capitolo, "flagRilevanteIva", "flagRilevanteIva");
		impostaFlag(result, capitolo, "flagImpegnabile", "flagImpegnabile");
		popolaFlagPerMemoria(result, capitolo);
	}
	
	/**
	 * Popola il flag per memoria, presente solo nel caso dei capitolo di previsione.
	 * 
	 * @param result   il wrapper da popolare
	 * @param capitolo il capitolo da wrappare
	 */
	private static void popolaFlagPerMemoria(ElementoCapitoloCodifiche result, Capitolo<?, ?> capitolo) {
		if(TipoCapitolo.CAPITOLO_ENTRATA_PREVISIONE.equals(capitolo.getTipoCapitolo()) || TipoCapitolo.CAPITOLO_USCITA_PREVISIONE.equals(capitolo.getTipoCapitolo())) {
			impostaFlag(result, capitolo, "flagPerMemoria", "flagCorsivoPerMemoria");
		}
	}
	
	/**
	 * Metodo per l'impostazione di un campo nel wrapper.
	 * 
	 * @param result    il wrapper da popolare
	 * @param capitolo  il capitolo da cui ottenere il campo
	 * @param clazz     la classe del campo
	 */
	@SuppressWarnings("unchecked")
	private static <T> void impostaCampo(ElementoCapitoloCodifiche result, Capitolo<?, ?> capitolo, Class<T> clazz) {
		String nomeCampo = clazz.getSimpleName();
		try {
			T campo = (T) capitolo.getClass().getMethod("get" + nomeCampo).invoke(capitolo);
			ElementoCapitoloCodifiche.class.getMethod("set" + nomeCampo, clazz).invoke(result, campo);
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			LOG.warn("impostaCampo", "Errore nell'impostazione del campo " + nomeCampo);
		}
	}
	
	/**
	 * Metodo per l'impostazione dei classificatori generici nel wrapper.
	 * 
	 * @param result    il wrapper da popolare
	 * @param capitolo  il capitolo da cui ottenere il campo
	 */
	private static void impostaClassificatoriGenerici(ElementoCapitoloCodifiche result, Capitolo<?, ?> capitolo) {
		try {
			List<ClassificatoreGenerico> listaClassificatoriGenerici = capitolo.getClassificatoriGenerici();
			impostaClassificatoreGenericoDaLista(result, listaClassificatoriGenerici);
		} catch (IllegalArgumentException e) {
			LOG.warn("impostaCampo", "Errore nell'impostazione del classificatore: " + e.getMessage());
		}
	}
	
	/**
	 * Imposta i classificatori generici a partire da una lista.
	 * 
	 * @param result                      il wrapper in cui injettare i classificatori
	 * @param listaClassificatoriGenerici la lista dei classificatori da injettare
	 * @throws IllegalArgumentException nel caso di errore nell'injezione del parametro 
	 */
	private static void impostaClassificatoreGenericoDaLista(ElementoCapitoloCodifiche result, List<ClassificatoreGenerico> listaClassificatoriGenerici) {
		for(ClassificatoreGenerico classificatoreGenerico : listaClassificatoriGenerici) {
			TipoClassificatore tipoClassificatore = classificatoreGenerico.getTipoClassificatore();
			if(tipoClassificatore == null || tipoClassificatore.getCodice() == null) {
				continue;
			}
			int numeroClassificatoreGenerico = Integer.parseInt(tipoClassificatore.getCodice().split("_")[1]);
			if(numeroClassificatoreGenerico > 35) {
				// Entrata: 36-50 => 1-15
				numeroClassificatoreGenerico -= 35;
			} else if (numeroClassificatoreGenerico > 30) {
				// Spesa: 31-35 => 11-15
				numeroClassificatoreGenerico -= 20;
			}
			
			String metodoDaInvocare = "setClassificatoreGenerico" + numeroClassificatoreGenerico;
			ReflectionUtil.invokeSetterMethod(result, metodoDaInvocare, ClassificatoreGenerico.class, classificatoreGenerico);
		}
	}
	
	/**
	 * Imposta il flag nel wrapper.
	 * 
	 * @param result           il wrapper da popolare
	 * @param capitolo         il capitolo da cui ottenere i dati
	 * @param nomeFlagCapitolo il nome del flag nel capitolo
	 * @param nomeFlagWrapper  il nome del flag nel wrapper
	 */
	private static void impostaFlag(ElementoCapitoloCodifiche result, Capitolo<?, ?> capitolo, String nomeFlagCapitolo, String nomeFlagWrapper) {
		String getterFlagCapitolo = "get" + StringUtils.capitalize(nomeFlagCapitolo);
		String setterFlagWrapper = "set" + StringUtils.capitalize(nomeFlagWrapper);
		try {
			Boolean flag = (Boolean) capitolo.getClass().getMethod(getterFlagCapitolo).invoke(capitolo);
			ElementoCapitoloCodifiche.class.getMethod(setterFlagWrapper, Boolean.class).invoke(result, flag);
		} catch (Exception e) {
			LOG.warn("impostaCampo", "Errore nell'impostazione del flag " + nomeFlagWrapper + ": " + e.getMessage());
		}
	}
	
	/**
	 * Ricostruisce una lista di dettagli di variazioni delle codifiche a partire da un wrapper.
	 * 
	 * @param elementoCapitoloCodifiche il wrapper
	 * 
	 * @return la lista dei dettaglio corrispondenti al wrapper
	 * @throws IllegalArgumentException nel caso in cui non sia possibile instanziare un capitolo
	 */
	public static List<DettaglioVariazioneCodificaCapitolo> unwrap(ElementoCapitoloCodifiche elementoCapitoloCodifiche) {
		List<DettaglioVariazioneCodificaCapitolo> result = new ArrayList<DettaglioVariazioneCodificaCapitolo>();
		List<ClassificatoreGenerico> listaClassificatoriGenerici = elementoCapitoloCodifiche.ottieniListaClassificatoriGenerici();
		List<ClassificatoreGerarchico> listaClassificatoriGerarchici = elementoCapitoloCodifiche.ottieniListaClassificatoriGerarchici();
		
		for(Integer i : elementoCapitoloCodifiche.getListaUidCapitolo()) {
			DettaglioVariazioneCodificaCapitolo temp = new DettaglioVariazioneCodificaCapitolo();
			Class<Capitolo<?, ?>> tipoCapitoloClass = elementoCapitoloCodifiche.getTipoCapitolo().getTipoCapitoloClass();
			Capitolo<?, ?> capitolo = null;
			try {
				capitolo = elementoCapitoloCodifiche.getTipoCapitolo().getTipoCapitoloClass().newInstance();
			} catch (InstantiationException e) {
				throw new IllegalArgumentException("InstantiationException nell'inizializzazione dei un capitolo di tipo " + tipoCapitoloClass.getSimpleName(), e);
			} catch (IllegalAccessException e) {
				throw new IllegalArgumentException("IllegalAccessException nell'inizializzazione dei un capitolo di tipo " + tipoCapitoloClass.getSimpleName(), e);
			}
			capitolo.setUid(i);
			
			capitolo.setNumeroCapitolo(elementoCapitoloCodifiche.getNumeroCapitolo());
			capitolo.setNumeroArticolo(elementoCapitoloCodifiche.getNumeroArticolo());
			capitolo.setNumeroUEB(elementoCapitoloCodifiche.getNumeroUEB());
			
			capitolo.setDescrizione(elementoCapitoloCodifiche.getDescrizioneCapitolo());
			capitolo.setDescrizioneArticolo(elementoCapitoloCodifiche.getDescrizioneArticolo());
			capitolo.setCategoriaCapitolo(elementoCapitoloCodifiche.getCategoriaCapitolo());
			capitolo.setNote(elementoCapitoloCodifiche.getNote());
			
			temp.setCapitolo(capitolo);
			temp.setClassificatoriGenerici(listaClassificatoriGenerici);
			temp.setClassificatoriGerarchici(listaClassificatoriGerarchici);
			result.add(temp);
		}
		
		return result;
	}
	
	/**
	 * Ricostruisce una lista di dettagli di variazioni delle codifiche a partire da una lista di wrapper.
	 * 
	 * @param listaElementoCapitoloCodifiche la lista di wrapper
	 * 
	 * @return la lista dei dettaglio corrispondenti ai wrapper
	 * 
	 * @throws IllegalArgumentException nel caso non sia possibile instanziare un capitolo
	 */
	public static List<DettaglioVariazioneCodificaCapitolo> unwrap(List<ElementoCapitoloCodifiche> listaElementoCapitoloCodifiche) {
		List<DettaglioVariazioneCodificaCapitolo> result = new ArrayList<DettaglioVariazioneCodificaCapitolo>();
		for(ElementoCapitoloCodifiche elemento : listaElementoCapitoloCodifiche) {
			List<DettaglioVariazioneCodificaCapitolo> temp = unwrap(elemento);
			result.addAll(temp);
		}
		return result;
	}
	
	/**
	 * Ottiene una lista di wrapper a partire da una lista di dettagli.
	 * 
	 * @param listaDettaglio la lista da cui ottenere i wrapper
	 * 
	 * @return la lista dei wrapper
	 */
	public static List<ElementoCapitoloCodifiche> getInstancesFromDettaglio(List<DettaglioVariazioneCodificaCapitolo> listaDettaglio) {
		List<ElementoCapitoloCodifiche> result = new ArrayList<ElementoCapitoloCodifiche>();
		
		for(DettaglioVariazioneCodificaCapitolo dettaglio : listaDettaglio) {
			if(!isPresentByTipoCapitoloNumeroCapitoloNumeroArticolo(result, dettaglio.getCapitolo())) {
				ElementoCapitoloCodifiche temp = getInstance(dettaglio);
				result.add(temp);
			}
		}
		return result;
	}
	
	/**
	 * Ottiene una lista di wrapper a partire da una lista di capitoli.
	 * @param <T> la tipizzazione del capitolo
	 * 
	 * @param listaCapitoli la lista da cui ottenere i wrapper
	 * 
	 * @return la lista dei wrapper
	 */
	public static <T extends Capitolo<?, ?>> List<ElementoCapitoloCodifiche> getInstancesFromCapitolo(List<T> listaCapitoli) {
		List<ElementoCapitoloCodifiche> result = new ArrayList<ElementoCapitoloCodifiche>();
		
		for(T capitolo : listaCapitoli) {
			if(!isPresentByTipoCapitoloNumeroCapitoloNumeroArticolo(result, capitolo)) {
				ElementoCapitoloCodifiche temp = getInstance(capitolo);
				result.add(temp);
			}
		}
		return result;
	}
	
	/**
	 * Controlla se l'elemento definito dal dettaglio della variazione &eacute; gi&agrave; stato wrappato nella lista.
	 * 
	 * @param lista    la lista in cui controllare la presenza dell'elemento
	 * @param capitolo il capitolo i cui dati sono da cercare
	 * 
	 * @return <code>true</code> se il capitolo &eacute; gi&agrave; presente; <code>false</code> altrimenti
	 */
	private static boolean isPresentByTipoCapitoloNumeroCapitoloNumeroArticolo(List<ElementoCapitoloCodifiche> lista, Capitolo<?, ?> capitolo) {
		for(ElementoCapitoloCodifiche e : lista) {
			boolean presente = e.getTipoCapitolo().equals(capitolo.getTipoCapitolo()) &&
					e.getNumeroCapitolo().equals(capitolo.getNumeroCapitolo()) &&
					e.getNumeroArticolo().equals(capitolo.getNumeroArticolo());
			if(presente) {
				e.getListaUidCapitolo().add(capitolo.getUid());
				return true;
			}
		}
		return false;
	}
	
}

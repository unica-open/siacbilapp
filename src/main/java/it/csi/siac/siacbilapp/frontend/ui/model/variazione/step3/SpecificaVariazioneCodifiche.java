/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.variazione.step3;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.SerializationUtils;
import org.springframework.util.ReflectionUtils;

import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.variazione.codifiche.ElementoCapitoloCodifiche;
import it.csi.siac.siacbilser.frontend.webservice.msg.ControllaAttributiModificabiliCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.ControllaClassificatoriModificabiliCapitoloResponse;
import it.csi.siac.siacbilser.model.CategoriaCapitolo;
import it.csi.siac.siacbilser.model.TipoCapitolo;
import it.csi.siac.siacbilser.model.TipologiaAttributo;
import it.csi.siac.siaccorser.model.ClassificatoreGenerico;
import it.csi.siac.siaccorser.model.ClassificatoreGerarchico;
import it.csi.siac.siaccorser.model.Codifica;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;

/**
 * Classe di model per la specificazione della variazione delle codifiche di un capitolo.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 11/12/2013
 *
 */
public class SpecificaVariazioneCodifiche extends SpecificaVariazioneModel {
	/** Per la serializzazione */
	private static final long serialVersionUID = -6498968854271471188L;
	
	private static final int NUMERO_CLASSIFICATORI_GENERICI_SPESA = 15;
	private static final int NUMERO_CLASSIFICATORI_GENERICI_ENTRATA = 15;
	private static final TipologiaClassificatore[] ARRAY_TIPOLOGIA_CLASSIFICATORE = TipologiaClassificatore.values();
	private static final TipologiaAttributo[] ARRAY_TIPOLOGIA_ATTRIBUTO = TipologiaAttributo.values();
	
	private String tipoCapitolo;
	
	private TipoCapitolo tipologiaCapitolo;
	
	private List<ElementoCapitoloCodifiche> listaElementoCapitoloCodifiche = new ArrayList<ElementoCapitoloCodifiche>();
	private Map<TipologiaClassificatore, Boolean> mappaClassificatoriModificabili = new EnumMap<TipologiaClassificatore, Boolean>(TipologiaClassificatore.class);
	private Map<TipologiaAttributo, Boolean> mappaAttributiModificabili = new EnumMap<TipologiaAttributo, Boolean>(TipologiaAttributo.class);
	private Map<String, Boolean> mappaDescrizioniModificabili = new HashMap<String, Boolean>(2, 1.0F);
	private Map<TipologiaClassificatore, List<? extends Codifica>> mappaClassificatori = new EnumMap<TipologiaClassificatore, List<? extends Codifica>>(TipologiaClassificatore.class);
	
	private List<CategoriaCapitolo> listaCategoriaCapitolo = new ArrayList<CategoriaCapitolo>();
	
	private ElementoCapitoloCodifiche elementoCapitoloCodifiche;
	private ElementoCapitoloCodifiche elementoCapitoloCodificheOld;
	
	/** Costruttore vuoto di default */
	public SpecificaVariazioneCodifiche() {
		super();
	}

	/**
	 * @return the tipoCapitolo
	 */
	public String getTipoCapitolo() {
		return tipoCapitolo;
	}

	/**
	 * @param tipoCapitolo the tipoCapitolo to set
	 */
	public void setTipoCapitolo(String tipoCapitolo) {
		this.tipoCapitolo = tipoCapitolo;
	}

	/**
	 * @return the tipologiaCapitolo
	 */
	public TipoCapitolo getTipologiaCapitolo() {
		return tipologiaCapitolo;
	}

	/**
	 * @param tipologiaCapitolo the tipologiaCapitolo to set
	 */
	public void setTipologiaCapitolo(TipoCapitolo tipologiaCapitolo) {
		this.tipologiaCapitolo = tipologiaCapitolo;
	}

	/**
	 * @return the listaElementoCapitoloCodifiche
	 */
	public List<ElementoCapitoloCodifiche> getListaElementoCapitoloCodifiche() {
		return listaElementoCapitoloCodifiche;
	}

	/**
	 * @param listaElementoCapitoloCodifiche the listaElementoCapitoloCodifiche to set
	 */
	public void setListaElementoCapitoloCodifiche(
			List<ElementoCapitoloCodifiche> listaElementoCapitoloCodifiche) {
		this.listaElementoCapitoloCodifiche = listaElementoCapitoloCodifiche;
	}

	/**
	 * @return the mappaClassificatoriModificabili
	 */
	public Map<TipologiaClassificatore, Boolean> getMappaClassificatoriModificabili() {
		return mappaClassificatoriModificabili;
	}

	/**
	 * @param mappaClassificatoriModificabili the mappaClassificatoriModificabili to set
	 */
	public void setMappaClassificatoriModificabili(Map<TipologiaClassificatore, Boolean> mappaClassificatoriModificabili) {
		this.mappaClassificatoriModificabili = mappaClassificatoriModificabili;
	}

	/**
	 * @return the mappaAttributiModificabili
	 */
	public Map<TipologiaAttributo, Boolean> getMappaAttributiModificabili() {
		return mappaAttributiModificabili;
	}

	/**
	 * @param mappaAttributiModificabili the mappaAttributiModificabili to set
	 */
	public void setMappaAttributiModificabili(
			Map<TipologiaAttributo, Boolean> mappaAttributiModificabili) {
		this.mappaAttributiModificabili = mappaAttributiModificabili;
	}

	/**
	 * @return the mappaDescrizioniModificabili
	 */
	public Map<String, Boolean> getMappaDescrizioniModificabili() {
		return mappaDescrizioniModificabili;
	}

	/**
	 * @param mappaDescrizioniModificabili the mappaDescrizioniModificabili to set
	 */
	public void setMappaDescrizioniModificabili(Map<String, Boolean> mappaDescrizioniModificabili) {
		this.mappaDescrizioniModificabili = mappaDescrizioniModificabili;
	}

	/**
	 * @return the mappaClassificatori
	 */
	public Map<TipologiaClassificatore, List<? extends Codifica>> getMappaClassificatori() {
		return mappaClassificatori;
	}

	/**
	 * @param mappaClassificatori the mappaClassificatori to set
	 */
	public void setMappaClassificatori(Map<TipologiaClassificatore, List<? extends Codifica>> mappaClassificatori) {
		this.mappaClassificatori = mappaClassificatori;
	}
	
	/**
	 * @return the listaCategoriaCapitolo
	 */
	public List<CategoriaCapitolo> getListaCategoriaCapitolo() {
		return listaCategoriaCapitolo;
	}

	/**
	 * @param listaCategoriaCapitolo the listaCategoriaCapitolo to set
	 */
	public void setListaCategoriaCapitolo(List<CategoriaCapitolo> listaCategoriaCapitolo) {
		this.listaCategoriaCapitolo = listaCategoriaCapitolo;
	}

	/**
	 * @return the elementoCapitoloCodifiche
	 */
	public ElementoCapitoloCodifiche getElementoCapitoloCodifiche() {
		return elementoCapitoloCodifiche;
	}

	/**
	 * @param elementoCapitoloCodifiche the elementoCapitoloCodifiche to set
	 */
	public void setElementoCapitoloCodifiche(
			ElementoCapitoloCodifiche elementoCapitoloCodifiche) {
		this.elementoCapitoloCodifiche = elementoCapitoloCodifiche;
	}
	
	/**
	 * @return the elementoCapitoloCodificheOld
	 */
	public ElementoCapitoloCodifiche getElementoCapitoloCodificheOld() {
		return elementoCapitoloCodificheOld;
	}

	/**
	 * @param elementoCapitoloCodificheOld the elementoCapitoloCodificheOld to set
	 */
	public void setElementoCapitoloCodificheOld(
			ElementoCapitoloCodifiche elementoCapitoloCodificheOld) {
		this.elementoCapitoloCodificheOld = elementoCapitoloCodificheOld;
	}
	
	/**
	 * @return the numeroClassificatoriGenericiSpesa
	 */
	public int getNumeroClassificatoriGenericiSpesa() {
		return NUMERO_CLASSIFICATORI_GENERICI_SPESA;
	}
	
	/**
	 * @return the numeroClassificatoriGenericiEntrata
	 */
	public int getNumeroClassificatoriGenericiEntrata() {
		return NUMERO_CLASSIFICATORI_GENERICI_ENTRATA;
	}
	
	/* **** Metodi di utilita' **** */
	
	/**
	 * Imposta i classificatori modificabili.
	 * 
	 * @param response la response da cui ottenere i classificatori modificabili
	 */
	public void impostaClassificatoriModificabili(ControllaClassificatoriModificabiliCapitoloResponse response) {
		// Pulisco la mappa
		mappaClassificatoriModificabili.clear();
		boolean unico = response.getStessoNumCapArt() <= 1L;
		// Imposto i classificatori
		for(TipologiaClassificatore tipologiaClassificatore : ARRAY_TIPOLOGIA_CLASSIFICATORE) {
			//Boolean modificabile = unico || response.isModificabileMassivo(tipologiaClassificatore);
			boolean isCDCorCDR = TipologiaClassificatore.CDC.equals(tipologiaClassificatore) || TipologiaClassificatore.CDR.equals(tipologiaClassificatore);
			Boolean modificabile = isCDCorCDR && (unico || response.isModificabileMassivo(tipologiaClassificatore));
			mappaClassificatoriModificabili.put(tipologiaClassificatore, modificabile);
		}
	}
	
	/**
	 * Imposta gli attributi modificabili.
	 * 
	 * @param response la response da cui ottenere gli attributi modificabili
	 */
	public void impostaAttributiModificabili(ControllaAttributiModificabiliCapitoloResponse response) {
		// Pulisco la mappa
		mappaAttributiModificabili.clear();
		//boolean unico = response.getStessoNumCapArt() <= 1L;
		// Imposto i classificatori
		for(TipologiaAttributo tipologiaAttributo : ARRAY_TIPOLOGIA_ATTRIBUTO) {
			//CR-			
			//Boolean modificabile = unico || response.isModificabileMassivo(tipologiaAttributo);
			mappaAttributiModificabili.put(tipologiaAttributo, Boolean.FALSE);
		}
	}
	
	/**
	 * Imposta le descrizioni modificabili.
	 * 
	 * @param response la response da cui ottenere gli attributi modificabili
	 */
	public void impostaDescrizioniModificabili(ControllaAttributiModificabiliCapitoloResponse response) {
		// Pulisco la mappa
		mappaDescrizioniModificabili.clear();
		boolean soloCapitoliStessoArticolo = response.getStessoNumCap().compareTo(response.getStessoNumCapArt()) == 0;
		
		mappaDescrizioniModificabili.put("DESCRIZIONE", soloCapitoliStessoArticolo);
		mappaDescrizioniModificabili.put("DESCRIZIONE_ARTICOLO", Boolean.TRUE);
	}

	/**
	 * Popola le codifiche a partire dalle liste
	 */
	public void popolaCodifiche() {
		impostaLaCodifica(TipologiaClassificatore.MISSIONE, elementoCapitoloCodifiche.getMissione());
		impostaLaCodifica(TipologiaClassificatore.PROGRAMMA, elementoCapitoloCodifiche.getProgramma());
		impostaLaCodifica(TipologiaClassificatore.CLASSIFICAZIONE_COFOG, elementoCapitoloCodifiche.getClassificazioneCofog());
		impostaLaCodifica(TipologiaClassificatore.TITOLO_SPESA, elementoCapitoloCodifiche.getTitoloSpesa());
		impostaLaCodifica(TipologiaClassificatore.MACROAGGREGATO, elementoCapitoloCodifiche.getMacroaggregato());
		
		impostaLaCodifica(TipologiaClassificatore.TITOLO_ENTRATA, elementoCapitoloCodifiche.getTitoloEntrata());
		impostaLaCodifica(TipologiaClassificatore.TIPOLOGIA, elementoCapitoloCodifiche.getTipologiaTitolo());
		impostaLaCodifica(TipologiaClassificatore.CATEGORIA, elementoCapitoloCodifiche.getCategoriaTipologiaTitolo());
		
		impostaLaCodificaGerarchica(TipologiaClassificatore.PDC, elementoCapitoloCodifiche.getElementoPianoDeiConti());
		
		impostaLaCodificaGerarchica(TipologiaClassificatore.SIOPE_SPESA, elementoCapitoloCodifiche.getSiopeSpesa());
		impostaLaCodificaGerarchica(TipologiaClassificatore.SIOPE_ENTRATA, elementoCapitoloCodifiche.getSiopeEntrata());
		
		impostaLaCodificaGerarchica(TipologiaClassificatore.CDC, elementoCapitoloCodifiche.getStrutturaAmministrativoContabile());
		
		impostaLaCodifica(TipologiaClassificatore.TIPO_FINANZIAMENTO, elementoCapitoloCodifiche.getTipoFinanziamento());
		impostaLaCodifica(TipologiaClassificatore.TIPO_FONDO, elementoCapitoloCodifiche.getTipoFondo());
		
		impostaLaCodifica(TipologiaClassificatore.RICORRENTE_SPESA, elementoCapitoloCodifiche.getRicorrenteEntrata());
		impostaLaCodifica(TipologiaClassificatore.RICORRENTE_ENTRATA, elementoCapitoloCodifiche.getRicorrenteSpesa());
		impostaLaCodifica(TipologiaClassificatore.PERIMETRO_SANITARIO_SPESA, elementoCapitoloCodifiche.getPerimetroSanitarioSpesa());
		impostaLaCodifica(TipologiaClassificatore.PERIMETRO_SANITARIO_ENTRATA, elementoCapitoloCodifiche.getPerimetroSanitarioEntrata());
		impostaLaCodifica(TipologiaClassificatore.TRANSAZIONE_UE_SPESA, elementoCapitoloCodifiche.getTransazioneUnioneEuropeaSpesa());
		impostaLaCodifica(TipologiaClassificatore.TRANSAZIONE_UE_ENTRATA, elementoCapitoloCodifiche.getTransazioneUnioneEuropeaEntrata());
		impostaLaCodifica(TipologiaClassificatore.POLITICHE_REGIONALI_UNITARIE, elementoCapitoloCodifiche.getPoliticheRegionaliUnitarie());
		
		impostaClassificatoriGenerici();
	}
	
	/**
	 * Imposta la codifica nel wrapper popolandolo dalla lista nella mappa dei classificatori.
	 * 
	 * @param tipologiaClassificatore la tipologia del classificatore
	 * @param codifica                la codifica da cercare, con uid popolato
	 */
	@SuppressWarnings("unchecked")
	private <T extends Codifica> void impostaLaCodifica(TipologiaClassificatore tipologiaClassificatore, T codifica) {
		if(codifica == null  || codifica.getUid() == 0) {
			return;
		}
		List<T> listaClassificatore = (List<T>) mappaClassificatori.get(tipologiaClassificatore);
		T entita = ComparatorUtils.searchByUid(listaClassificatore, codifica);
		
		// Popolamento dei dati della codifica
		if(codifica.getDataFineValidita() != null) {
			entita = SerializationUtils.clone(entita);
			entita.setDataFineValidita(codifica.getDataFineValidita());
		}
		Class<?> clazz = codifica.getClass();
		Method setter = ReflectionUtils.findMethod(ElementoCapitoloCodifiche.class, "set" + clazz.getSimpleName(), clazz);
		ReflectionUtils.invokeMethod(setter, elementoCapitoloCodifiche, entita);
	}
	
	/**
	 * Imposta i classificatori generici nel wrapper.
	 */
	@SuppressWarnings("unchecked")
	private <T extends Codifica> void impostaClassificatoriGenerici() {
		// Ciclo sui classificatori generici
		for(int i = 1; i <= 10; i++) {
			Method getter = ReflectionUtils.findMethod(ElementoCapitoloCodifiche.class, "getClassificatoreGenerico" + i);
			ClassificatoreGenerico classificatoreGenerico = (ClassificatoreGenerico) ReflectionUtils.invokeMethod(getter, elementoCapitoloCodifiche);
			// Se il classificatore è nullo, continuo con le iterazioni
			if(classificatoreGenerico == null) {
				continue;
			}
			TipologiaClassificatore tipologiaClassificatore = TipologiaClassificatore.fromCodice("CLASSIFICATORE_" + i);
			List<ClassificatoreGenerico> listaClassificatore = (List<ClassificatoreGenerico>) mappaClassificatori.get(tipologiaClassificatore);
			ClassificatoreGenerico entita = ComparatorUtils.searchByUid(listaClassificatore, classificatoreGenerico);
			if(classificatoreGenerico.getDataFineValidita() != null) {
				entita = SerializationUtils.clone(entita);
				entita.setDataFineValidita(classificatoreGenerico.getDataFineValidita());
			}
			Method setter = ReflectionUtils.findMethod(ElementoCapitoloCodifiche.class, "setClassificatoreGenerico" + i, ClassificatoreGenerico.class);
			ReflectionUtils.invokeMethod(setter, elementoCapitoloCodifiche, entita);
		}
	}
	
	@SuppressWarnings("unchecked")
	private <T extends ClassificatoreGerarchico> void impostaLaCodificaGerarchica(TipologiaClassificatore tipologiaClassificatore, T codifica) {
		if(codifica == null) {
			return;
		}
		List<T> listaClassificatore = (List<T>) mappaClassificatori.get(tipologiaClassificatore);
		T entita = ComparatorUtils.searchByUidWithChildren(listaClassificatore, codifica);
		if(codifica.getDataFineValidita() != null) {
			entita = SerializationUtils.clone(entita);
			entita.setDataFineValidita(codifica.getDataFineValidita());
		}
		Class<?> clazz = codifica.getClass();
		Method setter = ReflectionUtils.findMethod(ElementoCapitoloCodifiche.class, "set" + clazz.getSimpleName(), clazz);
		ReflectionUtils.invokeMethod(setter, elementoCapitoloCodifiche, entita);
	}
	
}

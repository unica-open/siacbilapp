/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
/*
 * 
 */
package it.csi.siac.siacgsaapp.frontend.ui.model.classifgsa;

import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siacbilser.model.Ambito;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacgenser.frontend.webservice.msg.AggiornaClassificatoreGSA;
import it.csi.siac.siacgenser.frontend.webservice.msg.AnnullaClassificatoreGSA;
import it.csi.siac.siacgenser.frontend.webservice.msg.InserisceClassificatoreGSA;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaClassificatoreGSA;
import it.csi.siac.siacgenser.model.ClassificatoreGSA;
import it.csi.siac.siacgenser.model.StatoOperativoClassificatoreGSA;

/**
 * Classe di model per l'inserimento della causale EP.
 * 
 * @author Elisa Chiari
 * @version 1.0.0 - 18/02/2017
 *
 */
public class GestisciClassificatoreGSAModel extends GenericBilancioModel {
	

	/** Per la serializzazione */
	private static final long serialVersionUID = -2737909917024097721L;



	/** Costruttore vuoto di default */
	public GestisciClassificatoreGSAModel() {
		setTitolo("Inserisci Classificatore GSA");
	}
	
	private ClassificatoreGSA classificatoreGSAPadreInElaborazione;
	private ClassificatoreGSA classificatoreGSAFiglioInElaborazione;
	
	private List<ClassificatoreGSA> listaClassificatoriGSATrovati  = new ArrayList<ClassificatoreGSA>();
	
	private ClassificatoreGSA classificatoreGSA;
	
	private List<StatoOperativoClassificatoreGSA> statiOperativiClassificatoreGSA =  new ArrayList<StatoOperativoClassificatoreGSA>();
	
	private StatoOperativoClassificatoreGSA statoOperativoClassificatoreDefault = StatoOperativoClassificatoreGSA.VALIDO;
	
	private int uidClassificatorePadre;
	private String descrizioneStatoOperativoClassificatore;
	
	

	/**
	 * Gets the classificatore GSA.
	 *
	 * @return the classificatoreGSA
	 */
	public ClassificatoreGSA getClassificatoreGSAPadreInElaborazione() {
		return classificatoreGSAPadreInElaborazione;
	}

	/**
	 * Sets the classificatore GSA.
	 *
	 * @param classificatoreGSAPadreInElaborazione the new classificatore GSA padre
	 */
	public void setClassificatoreGSAPadreInElaborazione(ClassificatoreGSA classificatoreGSAPadreInElaborazione) {
		this.classificatoreGSAPadreInElaborazione = classificatoreGSAPadreInElaborazione;
	}

	/**
	 * Gets the classificatore GSA figlio in elaborazione.
	 *
	 * @return the classificatoreGSAFiglioInElaborazione
	 */
	public ClassificatoreGSA getClassificatoreGSAFiglioInElaborazione() {
		return classificatoreGSAFiglioInElaborazione;
	}

	/**
	 * Sets the classificatore GSA figlio in elaborazione.
	 *
	 * @param classificatoreGSAFiglioInElaborazione the classificatoreGSAFiglioInElaborazione to set
	 */
	public void setClassificatoreGSAFiglioInElaborazione(ClassificatoreGSA classificatoreGSAFiglioInElaborazione) {
		this.classificatoreGSAFiglioInElaborazione = classificatoreGSAFiglioInElaborazione;
	}

	/**
	 * Gets the lista classificatori GSA.
	 *
	 * @return the listaClassificatoriGSA
	 */
	public List<ClassificatoreGSA> getListaClassificatoriGSATrovati() {
		return listaClassificatoriGSATrovati;
	}

	/**
	 * Sets the lista classificatori GSA.
	 *
	 * @param listaClassificatoriGSATrovati the new lista classificatori GSA trovati
	 */
	public void setListaClassificatoriGSATrovati(List<ClassificatoreGSA> listaClassificatoriGSATrovati) {
		this.listaClassificatoriGSATrovati = listaClassificatoriGSATrovati != null? listaClassificatoriGSATrovati : new ArrayList<ClassificatoreGSA>();
	}

	/**
	 * Gets the stato operativo classificatore default.
	 *
	 * @return the statoOperativoClassificatoreDefault
	 */
	public StatoOperativoClassificatoreGSA getStatoOperativoClassificatoreDefault() {
		return statoOperativoClassificatoreDefault;
	}

	/**
	 * Sets the stato operativo classificatore default.
	 *
	 * @param statoOperativoClassificatoreDefault the statoOperativoClassificatoreDefault to set
	 */
	public void setStatoOperativoClassificatoreDefault(StatoOperativoClassificatoreGSA statoOperativoClassificatoreDefault) {
		this.statoOperativoClassificatoreDefault = statoOperativoClassificatoreDefault;
	}
	
	
	// CREAZIONE DELLE REQUESTS
	
	/**
	 * @return the classificatoreGSA
	 */
	public ClassificatoreGSA getClassificatoreGSA() {
		return classificatoreGSA;
	}

	/**
	 * @param classificatoreGSA the classificatoreGSA to set
	 */
	public void setClassificatoreGSA(ClassificatoreGSA classificatoreGSA) {
		this.classificatoreGSA = classificatoreGSA;
	}
	

	/**
	 * Gets the stati operativi classificatore GSA.
	 *
	 * @return the statiOperativiClassificatoreGSA
	 */
	public List<StatoOperativoClassificatoreGSA> getStatiOperativiClassificatoreGSA() {
		return statiOperativiClassificatoreGSA;
	}

	/**
	 * Sets the stati operativi classificatore GSA.
	 * @param statiOperativiClassificatoreGSA the statiOperativiClassificatoreGSA to set
	 */
	public void setStatiOperativiClassificatoreGSA(List<StatoOperativoClassificatoreGSA> statiOperativiClassificatoreGSA) {
		this.statiOperativiClassificatoreGSA = statiOperativiClassificatoreGSA != null? statiOperativiClassificatoreGSA : new ArrayList<StatoOperativoClassificatoreGSA>();
	}

	/**
	 * @return the uidClassificatorePadre
	 */
	public int getUidClassificatorePadre() {
		return uidClassificatorePadre;
	}

	/**
	 * @param uidClassificatorePadre the uidClassificatorePadre to set
	 */
	public void setUidClassificatorePadre(int uidClassificatorePadre) {
		this.uidClassificatorePadre = uidClassificatorePadre;
	}

	/**
	 * Gets the descrizione stato operativo classificatore.
	 *
	 * @return the descrizione stato operativo classificatore
	 */
	public String getDescrizioneStatoOperativoClassificatore() {
		return descrizioneStatoOperativoClassificatore;
	}

	/**
	 * Sets the descrizione stato operativo classificatore.
	 *
	 * @param descrizioneStatoOperativoClassificatore the new descrizione stato operativo classificatore
	 */
	public void setDescrizioneStatoOperativoClassificatore(String descrizioneStatoOperativoClassificatore) {
		this.descrizioneStatoOperativoClassificatore = descrizioneStatoOperativoClassificatore;
	}

	/**
	 * Crea request inserisce classificatore GSA.
	 *
	 * @return la request creata
	 */
	public InserisceClassificatoreGSA creaRequestInserisceClassificatoreGSA() {
		InserisceClassificatoreGSA request = creaRequest(InserisceClassificatoreGSA.class);
		request.setClassificatoreGSA(getClassificatoreGSA());
		request.setBilancio(getBilancio());
		return request;
	}

	/**
	 * Imposta dati per inserimento classificatore GSA padre.
	 */
	public void impostaDatiPerInserimentoClassificatoreGSAPadre() {
		getClassificatoreGSA().setLivello(Integer.valueOf(0));
		getClassificatoreGSA().setStatoOperativoClassificatoreGSA(StatoOperativoClassificatoreGSA.VALIDO);
		getClassificatoreGSA().setAmbito(Ambito.AMBITO_GSA);
	}
	
	/**
	 * Imposta dati per inserimento classificatore GSA figlio.
	 */
	public void impostaDatiPerInserimentoClassificatoreGSAFiglio() {
		getClassificatoreGSA().setLivello(Integer.valueOf(1));
		ClassificatoreGSA cPadre = new ClassificatoreGSA();
		cPadre.setUid(getUidClassificatorePadre());
		getClassificatoreGSA().setClassificatoreGSAPadre(cPadre);
		getClassificatoreGSA().setStatoOperativoClassificatoreGSA(StatoOperativoClassificatoreGSA.VALIDO);
		getClassificatoreGSA().setAmbito(Ambito.AMBITO_GSA);
	}
	
	/**
	 * Imposta dati per aggiornamento classificatore.
	 */
	public void impostaDatiPerAggiornamentoClassificatore() {
		StatoOperativoClassificatoreGSA socGSA = StatoOperativoClassificatoreGSA.byDescrizione(getDescrizioneStatoOperativoClassificatore());
		getClassificatoreGSA().setStatoOperativoClassificatoreGSA(socGSA);
		getClassificatoreGSA().setAmbito(Ambito.AMBITO_GSA);
		ClassificatoreGSA cPadre = new ClassificatoreGSA();
		cPadre.setUid(getUidClassificatorePadre());
		getClassificatoreGSA().setClassificatoreGSAPadre(cPadre);
	}
	/**
	 * Crea request ricerca sintetica classificatore GSA.
	 *
	 * @return the ricerca sintetica classificatore GSA
	 */
	public RicercaSinteticaClassificatoreGSA creaRequestRicercaSinteticaClassificatoreGSA() {
		RicercaSinteticaClassificatoreGSA req = creaRequest(RicercaSinteticaClassificatoreGSA.class);
		getClassificatoreGSA().setAmbito(Ambito.AMBITO_GSA);
		req.setClassificatoreGSA(getClassificatoreGSA());
		req.setBilancio(getBilancio());
		req.setParametriPaginazione(new ParametriPaginazione(0, Integer.MAX_VALUE));
		return req;
	}
	
	/**
	 * Crea request aggiorna classificatore GSA.
	 *
	 * @return the aggiorna classificatore GSA
	 */
	public AggiornaClassificatoreGSA creaRequestAggiornaClassificatoreGSA() {
		AggiornaClassificatoreGSA req = creaRequest(AggiornaClassificatoreGSA.class);
		req.setClassificatoreGSA(getClassificatoreGSA());
		req.setBilancio(getBilancio());
		return req;
	}
	
	/**
	 * Crea request agnnulla classificatore GSA.
	 *
	 * @return the annulla classificatore GSA
	 */
	public AnnullaClassificatoreGSA creaRequestAgnnullaClassificatoreGSA() {
		AnnullaClassificatoreGSA req = creaRequest(AnnullaClassificatoreGSA.class);
		req.setClassificatoreGSA(getClassificatoreGSA());
		req.setBilancio(getBilancio());
		return req;
	}

}

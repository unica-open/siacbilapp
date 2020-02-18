/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.soggetto;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggetti;
import it.csi.siac.siacfinser.model.codifiche.ClasseSoggetto;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaSoggetto;
import it.csi.siac.siacfinser.model.ric.SorgenteDatiSoggetto;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;

/**
 * Classe di model per la ricerca soggetto
 * 
 * @author Osorio Alessandra
 * @version 1.0.0 - 05/03/2014
 * @version 1.1.0 - 30/11/2015 - gestione switch HR/SIAC
 * 
 */
public class SelezionaSoggettoModel extends GenericBilancioModel {
	
	/** Per la sincronizzazione */
	private static final long serialVersionUID = 9136594481507133714L;
	
	private Soggetto soggetto;
	private String codiceAmbito;
	private ClasseSoggetto classeSoggetto;
	
	private List<Soggetto> listaSoggetti = new ArrayList<Soggetto>();
	private Boolean maySearchHR;
	
	/** Costruttore vuoto di default */
	public SelezionaSoggettoModel() {
		super();
		setTitolo("Ricerca soggetti");
	}


	/* Getter e Setter */

	/**
	 * @return the soggetto
	 */
	public Soggetto getSoggetto() {
		return soggetto;
	}

	/**
	 * @param soggetto the soggetto to set
	 */
	public void setSoggetto(Soggetto soggetto) {
		this.soggetto = soggetto;
	}

	/**
	 * @return the codiceAmbito
	 */
	public String getCodiceAmbito() {
		return codiceAmbito;
	}


	/**
	 * @param codiceAmbito the codiceAmbito to set
	 */
	public void setCodiceAmbito(String codiceAmbito) {
		this.codiceAmbito = codiceAmbito;
	}


	/**
	 * @return the classeSoggetto
	 */
	public ClasseSoggetto getClasseSoggetto() {
		return classeSoggetto;
	}

	/**
	 * @param classeSoggetto the classeSoggetto to set
	 */
	public void setClasseSoggetto(ClasseSoggetto classeSoggetto) {
		this.classeSoggetto = classeSoggetto;
	}

	/**
	 * @return the listaSoggetti
	 */
	public List<Soggetto> getListaSoggetti() {
		return listaSoggetti;
	}


	/**
	 * @param listaSoggetti the listaSoggetti to set
	 */
	public void setListaSoggetti(List<Soggetto> listaSoggetti) {
		this.listaSoggetti = listaSoggetti;
	}

	/**
	 * @return the maySearchHR
	 */
	public Boolean getMaySearchHR() {
		return maySearchHR;
	}

	/**
	 * @param maySearchHR the maySearchHR to set
	 */
	public void setMaySearchHR(Boolean maySearchHR) {
		this.maySearchHR = maySearchHR;
	}
	
	/* Requests */

	/**
	 * Crea una request per il servizio di {@link RicercaSoggetti} del modulo Fin a partire dal Model.
	 * 
	 * @return la request creata
	 */
	public RicercaSoggetti creaRequestRicercaSoggetti() {
		RicercaSoggetti request = creaRequest(RicercaSoggetti.class);
		
		request.setEnte(getEnte());
		request.setNumPagina(0);
		request.setNumRisultatiPerPagina(GenericBilancioModel.ELEMENTI_PER_PAGINA_RICERCA);
		if(StringUtils.isNotBlank(getCodiceAmbito())){
			request.setCodiceAmbito(getCodiceAmbito());
		}
		
		request.setSorgenteDatiSoggetto(Boolean.TRUE.equals(getMaySearchHR()) && isGestioneHR() ? SorgenteDatiSoggetto.HR : SorgenteDatiSoggetto.SIAC);
		
		request.setParametroRicercaSoggetto(impostaParametroRicercaSoggetto());
		return request;
	}
	
	/* Utility */
	
	/**
	 * Metodo di utilit&agrave; per la creazione di uno storno UEB a partire dal model.
	 * 
	 * @return l'utility creata
	 */
	private ParametroRicercaSoggetto impostaParametroRicercaSoggetto() {
		ParametroRicercaSoggetto parametroRicercaSoggetto = new ParametroRicercaSoggetto();
		
		parametroRicercaSoggetto.setCodiceSoggetto(StringUtils.trimToNull(getSoggetto().getCodiceSoggetto()));
		parametroRicercaSoggetto.setMatricola(StringUtils.trimToNull(getSoggetto().getMatricola()));
		parametroRicercaSoggetto.setCodiceFiscale(StringUtils.trimToNull(getSoggetto().getCodiceFiscale()));
		parametroRicercaSoggetto.setDenominazione(StringUtils.trimToNull(getSoggetto().getDenominazione()));
		parametroRicercaSoggetto.setPartitaIva(StringUtils.trimToNull(getSoggetto().getPartitaIva()));
		//SIAC-6565-CR1215
		parametroRicercaSoggetto.setEmailPec(StringUtils.trimToNull(getSoggetto().getEmailPec()));
		parametroRicercaSoggetto.setCodDestinatario(StringUtils.trimToNull(getSoggetto().getCodDestinatario()));
		
		if(getClasseSoggetto() != null && getClasseSoggetto().getUid() != 0) {
			String uidClasse = Integer.toString(getClasseSoggetto().getUid());
			parametroRicercaSoggetto.setClasse(uidClasse);
		}
		
		return parametroRicercaSoggetto;
	}
	
}

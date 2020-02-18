/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.soggetto;

import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siacfin2ser.model.StatoOperativoModalitaPagamentoSoggetto;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggetti;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiave;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaSoggetto;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaSoggettoK;
import it.csi.siac.siacfinser.model.ric.SorgenteDatiSoggetto;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto;
import it.csi.siac.siacfinser.model.soggetto.sedesecondaria.SedeSecondariaSoggetto;
import it.csi.siac.siacfinser.model.soggetto.sedesecondaria.SedeSecondariaSoggetto.StatoOperativoSedeSecondaria;

/**
 * Classe di model per il caricamento dei dati del soggetto via matricola.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 27/03/2015
 * @version 1.1.0 - 30/11/2015 - gestione switch HR/SIAC
 *
 */
public class RicercaSoggettoPerMatricolaModel extends GenericBilancioModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -3960069781076558421L;
	
	private String codiceAmbito;
	private Integer uidSedeSecondariaSoggetto;
	
	private Soggetto soggetto;
	private List<SedeSecondariaSoggetto> listaSedeSecondariaSoggetto = new ArrayList<SedeSecondariaSoggetto>();
	private List<ModalitaPagamentoSoggetto> listaModalitaPagamentoSoggetto = new ArrayList<ModalitaPagamentoSoggetto>();
	private Boolean maySearchHR;
	
	/** Costruttore vuoto di default */
	public RicercaSoggettoPerMatricolaModel() {
		super();
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
	 * @return the uidSedeSecondariaSoggetto
	 */
	public Integer getUidSedeSecondariaSoggetto() {
		return uidSedeSecondariaSoggetto;
	}

	/**
	 * @param uidSedeSecondariaSoggetto the uidSedeSecondariaSoggetto to set
	 */
	public void setUidSedeSecondariaSoggetto(Integer uidSedeSecondariaSoggetto) {
		this.uidSedeSecondariaSoggetto = uidSedeSecondariaSoggetto;
	}

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
	 * @return the listaSedeSecondariaSoggetto
	 */
	public List<SedeSecondariaSoggetto> getListaSedeSecondariaSoggetto() {
		return listaSedeSecondariaSoggetto;
	}

	/**
	 * @param listaSedeSecondariaSoggetto the listaSedeSecondariaSoggetto to set
	 */
	public void setListaSedeSecondariaSoggetto(List<SedeSecondariaSoggetto> listaSedeSecondariaSoggetto) {
		this.listaSedeSecondariaSoggetto = listaSedeSecondariaSoggetto != null ? listaSedeSecondariaSoggetto : new ArrayList<SedeSecondariaSoggetto>();
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

	/**
	 * @return the listaSedeSecondariaSoggettoValide
	 */
	public List<SedeSecondariaSoggetto> getListaSedeSecondariaSoggettoValide() {
		List<SedeSecondariaSoggetto> result = new ArrayList<SedeSecondariaSoggetto>();
		for(SedeSecondariaSoggetto sss : listaSedeSecondariaSoggetto) {
			if(StatoOperativoSedeSecondaria.VALIDO.equals(sss.getStatoOperativoSedeSecondaria())) {
				result.add(sss);
			}
		}
		return result;
	}

	/**
	 * @return the listaModalitaPagamentoSoggetto
	 */
	public List<ModalitaPagamentoSoggetto> getListaModalitaPagamentoSoggetto() {
		return listaModalitaPagamentoSoggetto;
	}

	/**
	 * @param listaModalitaPagamentoSoggetto the listaModalitaPagamentoSoggetto to set
	 */
	public void setListaModalitaPagamentoSoggetto(List<ModalitaPagamentoSoggetto> listaModalitaPagamentoSoggetto) {
		this.listaModalitaPagamentoSoggetto = listaModalitaPagamentoSoggetto != null ? listaModalitaPagamentoSoggetto : new ArrayList<ModalitaPagamentoSoggetto>();
	}
	
	/**
	 * @return the listaModalitaPagamentoSoggettoValide
	 */
	public List<ModalitaPagamentoSoggetto> getListaModalitaPagamentoSoggettoValide() {
		List<ModalitaPagamentoSoggetto> result = new ArrayList<ModalitaPagamentoSoggetto>();
		for(ModalitaPagamentoSoggetto mps : listaModalitaPagamentoSoggetto) {
			if(StatoOperativoModalitaPagamentoSoggetto.VALIDO.getCodice().equalsIgnoreCase(mps.getCodiceStatoModalitaPagamento())) {
				result.add(mps);
			}
		}
		return result;
	}
	
	/* **** Requests **** */
	
	/**
	 * Crea una request per il servizio di {@link RicercaSoggetti}.
	 * 
	 * @return la request creata
	 */
	public RicercaSoggetti creaRequestRicercaSoggetti() {
		RicercaSoggetti request = creaRequest(RicercaSoggetti.class);
		
		request.setEnte(getEnte());
		
		ParametroRicercaSoggetto parametroRicercaSoggetto = new ParametroRicercaSoggetto();
		parametroRicercaSoggetto.setMatricola(getSoggetto().getMatricola());
		request.setParametroRicercaSoggetto(parametroRicercaSoggetto);
		
		request.setCodiceAmbito(getCodiceAmbito());
		request.setNumPagina(0);
		request.setNumRisultatiPerPagina(GenericBilancioModel.ELEMENTI_PER_PAGINA_RICERCA);
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaSoggettoPerChiave}.
	 * 
	 * @param sogg il soggetto da ricercare
	 * 
	 * @return la request creata
	 */
	public RicercaSoggettoPerChiave creaRequestRicercaSoggettoPerChiave(Soggetto sogg) {
		RicercaSoggettoPerChiave request = creaRequest(RicercaSoggettoPerChiave.class);
		
		request.setEnte(getEnte());
		
		ParametroRicercaSoggettoK parametroRicercaSoggettoK = new ParametroRicercaSoggettoK();
		parametroRicercaSoggettoK.setCodice(sogg.getCodiceSoggetto());
		parametroRicercaSoggettoK.setMatricola(sogg.getMatricola());
		request.setParametroSoggettoK(parametroRicercaSoggettoK);

		// Lotto P
		request.setSorgenteDatiSoggetto(Boolean.TRUE.equals(getMaySearchHR()) && isGestioneHR() ? SorgenteDatiSoggetto.HR : SorgenteDatiSoggetto.SIAC);
		request.setCodificaAmbito(getCodiceAmbito());
		
		return request;
	}
	
}

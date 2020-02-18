/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.allegatoatto;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceElenco;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioAllegatoAtto;
import it.csi.siac.siacfin2ser.model.AllegatoAtto;
import it.csi.siac.siacfin2ser.model.ElencoDocumentiAllegato;
import it.csi.siac.siacfin2ser.model.StatoOperativoModalitaPagamentoSoggetto;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiave;
import it.csi.siac.siacfinser.model.codifiche.CodificaFin;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaSoggettoK;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto;
import it.csi.siac.siacfinser.model.soggetto.sedesecondaria.SedeSecondariaSoggetto;
import it.csi.siac.siacfinser.model.soggetto.sedesecondaria.SedeSecondariaSoggetto.StatoOperativoSedeSecondaria;

/**
 * Classe di model per l'associazione con l'allegato atto.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 30/set/2014
 */
public class GenericAssociaAllegatoAttoModel extends GenericAllegatoAttoModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 9137812801016720388L;
	
	private Integer uidAllegatoAtto;
	// Soggetto
	private Soggetto soggetto;
	private SedeSecondariaSoggetto sedeSecondariaSoggetto;
	private ModalitaPagamentoSoggetto modalitaPagamentoSoggetto;
	
	private List<CodificaFin> listaClasseSoggetto = new ArrayList<CodificaFin>();
	private List<SedeSecondariaSoggetto> listaSedeSecondariaSoggetto = new ArrayList<SedeSecondariaSoggetto>();
	private List<ModalitaPagamentoSoggetto> listaModalitaPagamentoSoggetto = new ArrayList<ModalitaPagamentoSoggetto>();
	
	private Boolean proseguireConElaborazione;
	
	/**
	 * @return the uidAllegatoAtto
	 */
	public Integer getUidAllegatoAtto() {
		return uidAllegatoAtto;
	}

	/**
	 * @param uidAllegatoAtto the uidAllegatoAtto to set
	 */
	public void setUidAllegatoAtto(Integer uidAllegatoAtto) {
		this.uidAllegatoAtto = uidAllegatoAtto;
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
	 * @return the sedeSecondariaSoggetto
	 */
	public SedeSecondariaSoggetto getSedeSecondariaSoggetto() {
		return sedeSecondariaSoggetto;
	}

	/**
	 * @param sedeSecondariaSoggetto the sedeSecondariaSoggetto to set
	 */
	public void setSedeSecondariaSoggetto(SedeSecondariaSoggetto sedeSecondariaSoggetto) {
		this.sedeSecondariaSoggetto = sedeSecondariaSoggetto;
	}

	/**
	 * @return the modalitaPagamentoSoggetto
	 */
	public ModalitaPagamentoSoggetto getModalitaPagamentoSoggetto() {
		return modalitaPagamentoSoggetto;
	}

	/**
	 * @param modalitaPagamentoSoggetto the modalitaPagamentoSoggetto to set
	 */
	public void setModalitaPagamentoSoggetto(ModalitaPagamentoSoggetto modalitaPagamentoSoggetto) {
		this.modalitaPagamentoSoggetto = modalitaPagamentoSoggetto;
	}

	/**
	 * @return the listaClasseSoggetto
	 */
	public List<CodificaFin> getListaClasseSoggetto() {
		return listaClasseSoggetto;
	}

	/**
	 * @param listaClasseSoggetto the listaClasseSoggetto to set
	 */
	public void setListaClasseSoggetto(List<CodificaFin> listaClasseSoggetto) {
		this.listaClasseSoggetto = listaClasseSoggetto != null ? listaClasseSoggetto : new ArrayList<CodificaFin>();
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
	 * @return the listaSedeSecondariaSoggettoValide
	 */
	public List<SedeSecondariaSoggetto> getListaSedeSecondariaSoggettoValide() {
		List<SedeSecondariaSoggetto> lista = new ArrayList<SedeSecondariaSoggetto>();
		for(SedeSecondariaSoggetto sss : listaSedeSecondariaSoggetto) {
			if(StatoOperativoSedeSecondaria.VALIDO.equals(sss.getStatoOperativoSedeSecondaria())) {
				lista.add(sss);
			}
		}
		return lista;
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
	 * @return the proseguireConElaborazione
	 */
	public Boolean getProseguireConElaborazione() {
		return proseguireConElaborazione;
	}

	/**
	 * @param proseguireConElaborazione the proseguireConElaborazione to set
	 */
	public void setProseguireConElaborazione(Boolean proseguireConElaborazione) {
		this.proseguireConElaborazione = proseguireConElaborazione;
	}
	
	/**
	 * @return the listaModalitaPagamentoSoggettoFiltrate
	 */
	public List<ModalitaPagamentoSoggetto> getListaModalitaPagamentoSoggettoFiltrate() {
		List<ModalitaPagamentoSoggetto> result = new ArrayList<ModalitaPagamentoSoggetto>(getListaModalitaPagamentoSoggetto());
		if(getSedeSecondariaSoggetto() != null && getSedeSecondariaSoggetto().getUid() != 0) {
			SedeSecondariaSoggetto sss = ComparatorUtils.searchByUid(getListaSedeSecondariaSoggettoValide(), getSedeSecondariaSoggetto());
			String denominazione = StringUtils.trimToEmpty(sss.getDenominazione());
			Iterator<ModalitaPagamentoSoggetto> iterator = result.iterator();
			while(iterator.hasNext()) {
				ModalitaPagamentoSoggetto mps = iterator.next();
				if(!StatoOperativoModalitaPagamentoSoggetto.VALIDO.getCodice().equals(mps.getCodiceStatoModalitaPagamento())
						|| (!denominazione.equalsIgnoreCase(mps.getAssociatoA()))) {
					iterator.remove();
				}
			}
		}
		return result;
	}

	/* **** Request **** */

	/**
	 * Crea una request per il servizio di {@link InserisceElenco}.
	 * 
	 * @return la request creata
	 */
	public InserisceElenco creaRequestInserisceElenco() {
		InserisceElenco request = creaRequest(InserisceElenco.class);
		
		request.setBilancio(getBilancio());
		// Allegato
		AllegatoAtto allegatoAtto = new AllegatoAtto();
		allegatoAtto.setUid(getUidAllegatoAtto());
		// TODO: Elenco
		ElencoDocumentiAllegato elencoDocumentiAllegato = new ElencoDocumentiAllegato();
		request.setElencoDocumentiAllegato(elencoDocumentiAllegato);
		elencoDocumentiAllegato.setAllegatoAtto(allegatoAtto);
		
		popolaElencoDocumentiAllegato(elencoDocumentiAllegato);
		
		return request;
	}

	/**
	 * Popolamento dell'elenco dei documenti allegato.
	 * 
	 * @param elencoDocumentiAllegato l'elenco da popolare
	 */
	protected void popolaElencoDocumentiAllegato(ElencoDocumentiAllegato elencoDocumentiAllegato) {
		// To implement in subclasses
	}

	/**
	 * Crea una request per il servizio di {@link RicercaDettaglioAllegatoAtto}.
	 * 
	 * @return la request creata
	 */
	public RicercaDettaglioAllegatoAtto creaRequestRicercaDettaglioAllegatoAtto() {
		RicercaDettaglioAllegatoAtto request = creaRequest(RicercaDettaglioAllegatoAtto.class);
		
		AllegatoAtto aa = new AllegatoAtto();
		aa.setUid(getUidAllegatoAtto());
		request.setAllegatoAtto(aa);
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaSoggettoPerChiave}.
	 * 
	 * @return la request creata
	 */
	public RicercaSoggettoPerChiave creaRequestRicercaSoggettoPerChiave() {
		RicercaSoggettoPerChiave request = creaRequest(RicercaSoggettoPerChiave.class);
		
		request.setEnte(getEnte());
		ParametroRicercaSoggettoK parametroSoggettoK = new ParametroRicercaSoggettoK();
		parametroSoggettoK.setCodice(getSoggetto().getCodiceSoggetto());
		request.setParametroSoggettoK(parametroSoggettoK);
		
		return request;
	}

}

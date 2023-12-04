/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.ordinativo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacbilser.frontend.webservice.msg.ControllaDisponibilitaCassaContoVincolatoCapitolo;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EmetteOrdinativiDiIncassoDaElenco;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuoteDaEmettereEntrata;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;
import it.csi.siac.siacfinser.frontend.webservice.msg.DatiOpzionaliElencoSubTuttiConSoloGliIds;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentoPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentoPerChiaveOttimizzato;
import it.csi.siac.siacfinser.model.ordinativo.OrdinativoIncasso;
import it.csi.siac.siacfinser.model.provvisoriDiCassa.ProvvisorioDiCassa;
import it.csi.siac.siacfinser.model.ric.RicercaAccertamentoK;

/**
 * Classe di model per l'emissione degli ordinativi di incasso.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 12/11/2014
 *
 */
public class EmissioneOrdinativiIncassoModel extends GenericEmissioneOrdinativiModel {
	/** Per la serializzazione */
	private static final long serialVersionUID = -328936138257150309L;
	
	private CapitoloEntrataGestione capitolo;
	private OrdinativoIncasso ordinativo;
	
	private BigDecimal totaleEntrateCollegate;
	private List<SubdocumentoEntrata> listSubdocumenti = new ArrayList<SubdocumentoEntrata>();
	
	private List<ProvvisorioDiCassa> listProvvisorioDiCassa = new ArrayList<ProvvisorioDiCassa>();
	private List<ProvvisorioDiCassa> listProvvisorioDiCassaSelezionati = new ArrayList<ProvvisorioDiCassa>();
	
	private List<Integer> idsSubdocumentiEntrata = new ArrayList<Integer>();
	
	/** Costruttore vuoto di default */
	public EmissioneOrdinativiIncassoModel() {
		setTitolo("Emissione ordinativi di incasso");
	}

	/**
	 * @return the capitolo
	 */
	public CapitoloEntrataGestione getCapitolo() {
		return capitolo;
	}

	/**
	 * @param capitolo the capitolo to set
	 */
	public void setCapitolo(CapitoloEntrataGestione capitolo) {
		this.capitolo = capitolo;
	}

	/**
	 * @return the ordinativo
	 */
	public OrdinativoIncasso getOrdinativo() {
		return ordinativo;
	}

	/**
	 * @param ordinativo the ordinativo to set
	 */
	public void setOrdinativo(OrdinativoIncasso ordinativo) {
		this.ordinativo = ordinativo;
	}

	/**
	 * @return the totaleEntrateCollegate
	 */
	public BigDecimal getTotaleEntrateCollegate() {
		return totaleEntrateCollegate;
	}

	/**
	 * @param totaleEntrateCollegate the totaleEntrateCollegate to set
	 */
	public void setTotaleEntrateCollegate(BigDecimal totaleEntrateCollegate) {
		this.totaleEntrateCollegate = totaleEntrateCollegate;
	}

	/**
	 * @return the listSubdocumenti
	 */
	public List<SubdocumentoEntrata> getListSubdocumenti() {
		return listSubdocumenti;
	}

	/**
	 * @param listSubdocumenti the listSubdocumenti to set
	 */
	public void setListSubdocumenti(List<SubdocumentoEntrata> listSubdocumenti) {
		this.listSubdocumenti = listSubdocumenti != null ? listSubdocumenti : new ArrayList<SubdocumentoEntrata>();
	}
	
	/**
	 * @return the listProvvisorioDiCassa
	 */
	public List<ProvvisorioDiCassa> getListProvvisorioDiCassa() {
		return listProvvisorioDiCassa;
	}

	/**
	 * @param listProvvisorioDiCassa the listProvvisorioDiCassa to set
	 */
	public void setListProvvisorioDiCassa(List<ProvvisorioDiCassa> listProvvisorioDiCassa) {
		this.listProvvisorioDiCassa = listProvvisorioDiCassa;
	}

	@Override
	public void popolaIdsSubdocElaborati() {
		for (SubdocumentoEntrata subdocumentoEntrata : getListSubdocumenti()) {
			getUidsElaborati().add(Integer.valueOf(subdocumentoEntrata.getUid()));
		}
	}
	/* **** Requests **** */

	/**
	 * Crea una request per il servizio di {@link EmetteOrdinativiDiIncassoDaElenco} tramite l'elenco.
	 * 
	 * @return la request creata
	 */
	public EmetteOrdinativiDiIncassoDaElenco creaRequestEmetteOrdinativiDiIncassoDaElencoByElenco() {
		EmetteOrdinativiDiIncassoDaElenco request = creaRequest(EmetteOrdinativiDiIncassoDaElenco.class);
		
		popolaDatiBaseRequest(request);
		request.setElenchi(getListElenchi());
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link EmetteOrdinativiDiIncassoDaElenco} tramite le quote.
	 * 
	 * @return la request creata
	 */
	public EmetteOrdinativiDiIncassoDaElenco creaRequestEmetteOrdinativiDiIncassoDaElencoByQuota() {
		EmetteOrdinativiDiIncassoDaElenco request = creaRequest(EmetteOrdinativiDiIncassoDaElenco.class);
		
		popolaDatiBaseRequest(request);
		request.setSubdocumenti(getListSubdocumenti());
		
		return request;
	}

	/**
	 * Crea una request per il servizio di {@link RicercaQuoteDaEmettereEntrata}.
	 * 
	 * @return la request creata
	 */
	public RicercaQuoteDaEmettereEntrata creaRequestRicercaQuoteDaEmettereEntrata() {
		RicercaQuoteDaEmettereEntrata request = creaRequest(RicercaQuoteDaEmettereEntrata.class);
		
		if(getElencoDocumentiAllegato() != null) {
			request.setAnnoElenco(getElencoDocumentiAllegato().getAnno());
			request.setNumeroElenco(getElencoDocumentiAllegato().getNumero());
		}
		request.setNumeroElencoDa(getNumeroElencoDa());
		request.setNumeroElencoA(getNumeroElencoA());
		
		request.setSoggetto(getSoggetto());
		
		if(getAttoAmministrativo() != null) {
			request.setAnnoProvvedimento(getAttoAmministrativo().getAnno() == 0 ? null : getAttoAmministrativo().getAnno());
			request.setNumeroProvvedimento(getAttoAmministrativo().getNumero() == 0 ? null : getAttoAmministrativo().getNumero());
		}
		request.setTipoAtto(impostaEntitaFacoltativa(getTipoAtto()));
		request.setStruttAmmContabile(impostaEntitaFacoltativa(getStrutturaAmministrativoContabile()));
		
		if(getCapitolo() != null && getCapitolo().getNumeroCapitolo() != null && getCapitolo().getNumeroArticolo() != null) {
			request.setAnnoCapitolo(getCapitolo().getAnnoCapitolo());
			request.setNumeroCapitolo(getCapitolo().getNumeroCapitolo());
			request.setNumeroArticolo(getCapitolo().getNumeroArticolo());
			request.setNumeroUEB(getCapitolo().getNumeroUEB());
		}
		
		request.setFlagConvalidaManuale(Boolean.TRUE);
		//distinta criterio ricerca
		request.setDistinta(impostaEntitaFacoltativa(getDistintaDaCercare()));
		request.setParametriPaginazione(creaParametriPaginazione());
		
		request.setListProvvisorioDiCassa(getListProvvisorioDiCassa());
		
		return request;
	}
	
	@Override
	protected void creaParametriRicercaCapitolo(StringBuilder sb) {
		boolean done = false;
		if(getCapitolo() != null && getCapitolo().getNumeroCapitolo() != null && getCapitolo().getNumeroArticolo() != null) {
			done = true;
			sb.append("<b>Capitolo : </b>")
				.append(getCapitolo().getNumeroCapitolo())
				.append("/")
				.append(getCapitolo().getNumeroArticolo());
			if(isGestioneUEB() && getCapitolo().getNumeroUEB() != null) {
				sb.append("/")
					.append(getCapitolo().getNumeroUEB());
			}
		}
		if(done) {
			sb.append("<br/>");
		}
	}
	
	/**
	 * Popolamento dei dati di base per la request.
	 * 
	 * @param request la request da popolare
	 */
	private void popolaDatiBaseRequest(EmetteOrdinativiDiIncassoDaElenco request) {
		request.setBilancio(getBilancio());
		request.setContoTesoreria(getContoTesoreria());
		//distinta emissione
		request.setDistinta(getDistinta());
		request.setFlagConvalidaManuale(Boolean.TRUE);
		request.setNote(getNota());
		//CR 3445
		request.setCodiceBollo(getCodiceBollo());
		request.setDataScadenza(getDataScadenza());
		//SIAC-5048
		request.setFlagNoDataScadenza(Boolean.TRUE.equals(isFlagNoDataScadenza()));
		//SIAC-6206
		request.setClassificatoreStipendi(getClassificatoreStipendi());
		//SIAC-6175
		request.setFlagDaTrasmettere(getFlagDaTrasmettere());
	}

	/**
	 * @return the listProvvisorioDiCassaSelezionati
	 */
	public List<ProvvisorioDiCassa> getListProvvisorioDiCassaSelezionati() {
		return listProvvisorioDiCassaSelezionati;
	}

	/**
	 * @param listProvvisorioDiCassaSelezionati the listProvvisorioDiCassaSelezionati to set
	 */
	public void setListProvvisorioDiCassaSelezionati(List<ProvvisorioDiCassa> listProvvisorioDiCassaSelezionati) {
		this.listProvvisorioDiCassaSelezionati = listProvvisorioDiCassaSelezionati;
	}
	
	public List<Integer> getIdsSubdocumentiEntrata() {
		return idsSubdocumentiEntrata;
	}

	public void setIdsSubdocumentiEntrata(List<Integer> idsSubdocumentiEntrata) {
		this.idsSubdocumentiEntrata = idsSubdocumentiEntrata;
	}

	/**
	 * Crea una request per il servizio di {@link RicercaAccertamentoPerChiave}.
	 * 
	 * @return la request creata
	 */
	//SIAC-7470: ricarico l'accertamento di cui ho bisogno
	public RicercaAccertamentoPerChiaveOttimizzato creaRequestRicercaAccertamentoPerChiaveOttimizzato(int anno, BigDecimal numero) {
		RicercaAccertamentoPerChiaveOttimizzato request = creaPaginazioneRequest(RicercaAccertamentoPerChiaveOttimizzato.class);
		request.setEnte(getEnte());
		RicercaAccertamentoK utility = new RicercaAccertamentoK();
		utility.setAnnoEsercizio(getAnnoEsercizioInt());
		utility.setAnnoAccertamento(anno);
		utility.setNumeroAccertamento(numero);
		request.setpRicercaAccertamentoK(utility);
		request.setCaricaSub(true);
		request.setSubPaginati(true);
		DatiOpzionaliElencoSubTuttiConSoloGliIds datiOpzionaliElencoSubTuttiConSoloGliIds = new DatiOpzionaliElencoSubTuttiConSoloGliIds();
		datiOpzionaliElencoSubTuttiConSoloGliIds.setEscludiAnnullati(true);
		request.setDatiOpzionaliElencoSubTuttiConSoloGliIds(datiOpzionaliElencoSubTuttiConSoloGliIds);
		return request;
	}
	
	@Override
	protected void impostaSubdocumentiSuRequestContoVincolato(ControllaDisponibilitaCassaContoVincolatoCapitolo request) {
		request.setIdsSubdocumentiEntrata(getIdsSubdocumentiEntrata());
		
	}

}

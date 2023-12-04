/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.ordinativo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import it.csi.siac.siacbilser.frontend.webservice.msg.ControllaDisponibilitaCassaCapitoloByMovimento;
import it.csi.siac.siacbilser.frontend.webservice.msg.ControllaDisponibilitaCassaContoVincolatoCapitolo;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siaccommon.model.ModelDetailEnum;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EmetteOrdinativiDiPagamentoDaElenco;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaElencoDaEmettere;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuoteDaEmettereSpesa;
import it.csi.siac.siacfin2ser.model.CommissioniDocumento;
import it.csi.siac.siacfin2ser.model.ElencoDocumentiAllegatoModelDetail;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfinser.frontend.webservice.msg.DatiOpzionaliElencoSubTuttiConSoloGliIds;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveOttimizzato;
import it.csi.siac.siacfinser.model.codifiche.CommissioneDocumento;
import it.csi.siac.siacfinser.model.ordinativo.OrdinativoPagamento;
import it.csi.siac.siacfinser.model.ric.RicercaImpegnoK;

/**
 * Classe di model per l'emissione degli ordinativi di pagamento.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 25/11/2014
 *
 */
public class EmissioneOrdinativiPagamentoModel extends GenericEmissioneOrdinativiModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -1705098611119262129L;
	
	private CapitoloUscitaGestione capitolo;
	private OrdinativoPagamento ordinativo;
	
	private BigDecimal totaleSpeseCollegate;
	private List<SubdocumentoSpesa> listSubdocumenti = new ArrayList<SubdocumentoSpesa>();
	
	//SIAC-5252
	private List<Integer> idsSubdocumentiSpesa = new ArrayList<Integer>();
	private List<Integer> idsElenchi = new ArrayList<Integer>();

	
	
	/** Costruttore vuoto di default */
	public EmissioneOrdinativiPagamentoModel() {
		setTitolo("Emissione ordinativi di pagamento");
	}



	/**
	 * @return the capitolo
	 */
	public CapitoloUscitaGestione getCapitolo() {
		return capitolo;
	}

	/**
	 * @param capitolo the capitolo to set
	 */
	public void setCapitolo(CapitoloUscitaGestione capitolo) {
		this.capitolo = capitolo;
	}

	/**
	 * @return the ordinativo
	 */
	public OrdinativoPagamento getOrdinativo() {
		return ordinativo;
	}

	/**
	 * @param ordinativo the ordinativo to set
	 */
	public void setOrdinativo(OrdinativoPagamento ordinativo) {
		this.ordinativo = ordinativo;
	}

	/**
	 * @return the totaleSpeseCollegate
	 */
	public BigDecimal getTotaleSpeseCollegate() {
		return totaleSpeseCollegate;
	}

	/**
	 * @param totaleSpeseCollegate the totaleSpeseCollegate to set
	 */
	public void setTotaleSpeseCollegate(BigDecimal totaleSpeseCollegate) {
		this.totaleSpeseCollegate = totaleSpeseCollegate;
	}

	/**
	 * @return the listSubdocumenti
	 */
	public List<SubdocumentoSpesa> getListSubdocumenti() {
		return listSubdocumenti;
	}

	/**
	 * @param listSubdocumenti the listSubdocumenti to set
	 */
	public void setListSubdocumenti(List<SubdocumentoSpesa> listSubdocumenti) {
		this.listSubdocumenti = listSubdocumenti != null ? listSubdocumenti : new ArrayList<SubdocumentoSpesa>();
	}
	
	/* **** Requests **** */

	/**
	 * @return the idsSubdocumentiSpesa
	 */
	public List<Integer> getIdsSubdocumentiSpesa() {
		return idsSubdocumentiSpesa;
	}



	/**
	 * @param idsSubdocumentiSpesa the idsSubdocumentiSpesa to set
	 */
	public void setIdsSubdocumentiSpesa(List<Integer> idsSubdocumentiSpesa) {
		this.idsSubdocumentiSpesa = idsSubdocumentiSpesa != null ? idsSubdocumentiSpesa : new ArrayList<Integer>();
	}



	/**
	 * @return the idsElenchi
	 */
	public List<Integer> getIdsElenchi() {
		return idsElenchi;
	}



	/**
	 * @param idsElenchi the idsElenchi to set
	 */
	public void setIdsElenchi(List<Integer> idsElenchi) {
		this.idsElenchi = idsElenchi != null ? idsElenchi : new ArrayList<Integer>();
	}

	/**
	 * Popola ids subdoc elaborati.
	 */
	@Override
	public void popolaIdsSubdocElaborati() {
		for (SubdocumentoSpesa subdocumentoSpesa :  getListSubdocumenti()) {
			getUidsElaborati().add(Integer.valueOf(subdocumentoSpesa.getUid()));
		}
	}
	
	/**
	 * Crea una request per il servizio di {@link EmetteOrdinativiDiPagamentoDaElenco} tramite l'elenco.
	 * 
	 * @return la request creata
	 */
	public EmetteOrdinativiDiPagamentoDaElenco creaRequestEmetteOrdinativiDiPagamentoDaElencoByElenco() {
		EmetteOrdinativiDiPagamentoDaElenco request = creaRequest(EmetteOrdinativiDiPagamentoDaElenco.class);
		
		popolaDatiBaseRequest(request);
		request.setElenchi(getListElenchi());
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaImpegnoPerChiave}.
	 * 
	 * @return la request creata
	 */
	//SIAC-7470: ricarico l'impegno di cui ho bisogno
	public RicercaImpegnoPerChiaveOttimizzato creaRequestRicercaImpegnoPerChiaveOttimizzato(int anno, BigDecimal numero) {
		RicercaImpegnoPerChiaveOttimizzato request = creaPaginazioneRequest(RicercaImpegnoPerChiaveOttimizzato.class);
		request.setEnte(getEnte());
		RicercaImpegnoK utility = new RicercaImpegnoK();
		utility.setAnnoEsercizio(getAnnoEsercizioInt());
		utility.setAnnoImpegno(anno);
		utility.setNumeroImpegno(numero);
		request.setpRicercaImpegnoK(utility);
		request.setCaricaSub(true);
		request.setSubPaginati(true);
		DatiOpzionaliElencoSubTuttiConSoloGliIds datiOpzionaliElencoSubTuttiConSoloGliIds = new DatiOpzionaliElencoSubTuttiConSoloGliIds();
		datiOpzionaliElencoSubTuttiConSoloGliIds.setEscludiAnnullati(true);
		request.setDatiOpzionaliElencoSubTuttiConSoloGliIds(datiOpzionaliElencoSubTuttiConSoloGliIds);
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link EmetteOrdinativiDiPagamentoDaElenco} tramite la quota.
	 * 
	 * @return la request creata
	 */
	public EmetteOrdinativiDiPagamentoDaElenco creaRequestEmetteOrdinativiDiPagamentoDaElencoByQuota() {
		EmetteOrdinativiDiPagamentoDaElenco request = creaRequest(EmetteOrdinativiDiPagamentoDaElenco.class);
		
		popolaDatiBaseRequest(request);
		request.setSubdocumenti(getListSubdocumenti());
		return request;
	}

	/**
	 * Crea una request per il servizio di {@link RicercaQuoteDaEmettereSpesa}.
	 * 
	 * @return la request creata
	 */
	public RicercaQuoteDaEmettereSpesa creaRequestRicercaQuoteDaEmettereSpesa() {
		RicercaQuoteDaEmettereSpesa request = creaRequest(RicercaQuoteDaEmettereSpesa.class);
		
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
	private void popolaDatiBaseRequest(EmetteOrdinativiDiPagamentoDaElenco request) {
		request.setBilancio(getBilancio());
		request.setContoTesoreria(getContoTesoreria());
		//distinta emissione
		request.setDistinta(getDistinta());
		request.setFlagConvalidaManuale(Boolean.TRUE);
		request.setNote(getNota());
		//CR 3445
		request.setCodiceBollo(getCodiceBollo());
		//task-291
		convertiCommissioneDocumentoToCommissioniDocumento(request, getCommissioneDocumento());
		request.setDataScadenza(getDataScadenza());
		//SIAC-5048
		request.setFlagNoDataScadenza(Boolean.TRUE.equals(isFlagNoDataScadenza()));
		//SIAC-6206
		request.setClassificatoreStipendi(getClassificatoreStipendi());
		//SIAC-6175
		request.setFlagDaTrasmettere(getFlagDaTrasmettere());
	}
	
	//task-291
	private void convertiCommissioneDocumentoToCommissioniDocumento(EmetteOrdinativiDiPagamentoDaElenco request, CommissioneDocumento commissione) {
		for(CommissioniDocumento lista : Arrays.asList(CommissioniDocumento.values())){
			if(lista.getDescrizione().toUpperCase().equals(commissione.getDescrizione().toUpperCase())){
				request.setCommissioniDocumento(lista);	
			}
		}
	}



	/**
	 * Crea request controlla disponibilita cassa capitolo by movimento.
	 *
	 * @return la request creata
	 */
	public ControllaDisponibilitaCassaCapitoloByMovimento creaRequestControllaDisponibilitaCassaCapitoloByMovimento() {
		ControllaDisponibilitaCassaCapitoloByMovimento request = creaRequest(ControllaDisponibilitaCassaCapitoloByMovimento.class);
		request.setBilancio(getBilancio());
		request.setIdsSubdocumentiSpesa(getIdsSubdocumentiSpesa());
		request.setIdsElenchi(getIdsElenchi());
		
		return request;
	}
	
	@Override
	public RicercaElencoDaEmettere creaRequestRicercaElencoDaEmettere() {
		RicercaElencoDaEmettere requestRicercaElencoDaEmettere = super.creaRequestRicercaElencoDaEmettere();
		requestRicercaElencoDaEmettere.setModelDetails(new ModelDetailEnum[] {
				ElencoDocumentiAllegatoModelDetail.Stato,
				ElencoDocumentiAllegatoModelDetail.TotaleDaPagareIncassare,
				ElencoDocumentiAllegatoModelDetail.TotaleQuoteSpesaEntrata,
				ElencoDocumentiAllegatoModelDetail.ContieneQuoteACopertura,
				ElencoDocumentiAllegatoModelDetail.SubdocumentiTotale,
				ElencoDocumentiAllegatoModelDetail.TotaleDaConvalidareSpesaEntrata,
				ElencoDocumentiAllegatoModelDetail.HasImpegnoConfermaDataFineValiditaDurc
				});
		return requestRicercaElencoDaEmettere;
	}



	@Override
	protected void impostaSubdocumentiSuRequestContoVincolato(ControllaDisponibilitaCassaContoVincolatoCapitolo request) {
		request.setIdsSubdocumentiSpesa(getIdsSubdocumentiSpesa());
		
	}
	

}

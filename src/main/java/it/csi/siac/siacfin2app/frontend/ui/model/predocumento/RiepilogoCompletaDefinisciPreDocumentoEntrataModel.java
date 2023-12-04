/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.predocumento;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import it.csi.siac.siaccorser.model.paginazione.ListaPaginataImpl;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.DefiniscePreDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaPreDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTotaliPreDocumentoEntrataPerStato;
import it.csi.siac.siacfin2ser.model.PreDocumentoEntrata;
import it.csi.siac.siacfinser.model.provvisoriDiCassa.ProvvisorioDiCassa;

/**
 * The Class RiepilogoCompletaDefinisciPreDocumentoEntrataModel.
 */
public class RiepilogoCompletaDefinisciPreDocumentoEntrataModel extends BaseCompletaDefinisciPreDocumentoEntrataModel {

	/** Per la serializzazione */
	private static final long serialVersionUID = 1287870370537741006L;
	
	private ListaPaginataImpl<PreDocumentoEntrata> listaPreDocEntrata;
	
	private List<Integer> listaUid = new ArrayList<Integer>();
	
	private boolean inviaTutti = Boolean.FALSE;
	private boolean riepilogoCompletaDefinisci = Boolean.FALSE;

	private BigDecimal importoTotale;
	
	private Integer idOperazioneAsincrona;
	private ProvvisorioDiCassa provvisorioCassa;
	

	/**
	 * Instantiates a new riepilogo completa definisci pre documento entrata model.
	 */
	public RiepilogoCompletaDefinisciPreDocumentoEntrataModel() {
		setTitolo("RIEPILOGO COMPLETA DEFINISCI PREDISPOSIZIONE D'INCASSO");
	}

	
	/**
	 * @return the listaPreDocEntrata
	 */
	public ListaPaginataImpl<PreDocumentoEntrata> getListaPreDocEntrata() {
		return listaPreDocEntrata;
	}

	/**
	 * @param listaPreDocEntrata the listaPreDocEntrata to set
	 */
	public void setListaPreDocEntrata(ListaPaginataImpl<PreDocumentoEntrata> listaPreDocEntrata) {
		this.listaPreDocEntrata = listaPreDocEntrata;
	}

	/**
	 * @return the listaUid
	 */
	public List<Integer> getListaUid() {
		return listaUid;
	}

	/**
	 * @param listaUid the listaUid to set
	 */
	public void setListaUid(List<Integer> listaUid) {
		this.listaUid = listaUid;
	}

	/**
	 * @return the inviaTutti
	 */
	public boolean isInviaTutti() {
		return inviaTutti;
	}

	/**
	 * @param inviaTutti the inviaTutti to set
	 */
	public void setInviaTutti(boolean inviaTutti) {
		this.inviaTutti = inviaTutti;
	}

	/**
	 * @return the riepilogoCompletaDefinisci
	 */
	public boolean isRiepilogoCompletaDefinisci() {
		return riepilogoCompletaDefinisci;
	}
	
	/**
	 * @param riepilogoCompletaDefinisci the riepilogoCompletaDefinisci to set
	 */
	public void setRiepilogoCompletaDefinisci(boolean riepilogoCompletaDefinisci) {
		this.riepilogoCompletaDefinisci = riepilogoCompletaDefinisci;
	}

	/**
	 * @return the importoTotale
	 */
	public BigDecimal getImportoTotale() {
		return importoTotale;
	}

	/**
	 * @param importoTotale the importoTotale to set
	 */
	public void setImportoTotale(BigDecimal importoTotale) {
		this.importoTotale = importoTotale;
	}

	/**
	 * @return the idOperazioneAsincrona
	 */
	public Integer getIdOperazioneAsincrona() {
		return idOperazioneAsincrona;
	}

	/**
	 * @param idOperazioneAsincrona the idOperazioneAsincrona to set
	 */
	public void setIdOperazioneAsincrona(Integer idOperazioneAsincrona) {
		this.idOperazioneAsincrona = idOperazioneAsincrona;
	}

	@Override
	public ProvvisorioDiCassa getProvvisorioCassa() {
		return provvisorioCassa;
	}

	@Override
	public void setProvvisorioCassa(ProvvisorioDiCassa provvisorioCassa) {
		this.provvisorioCassa = provvisorioCassa;
	}
	



	public RicercaTotaliPreDocumentoEntrataPerStato creaRequestRicercaTotaliPreDocumentoEntrataPerRiepilogoCompletaDefinisci(RicercaSinteticaPreDocumentoEntrata reqRicerca) {
		
		RicercaTotaliPreDocumentoEntrataPerStato req = creaRequest(RicercaTotaliPreDocumentoEntrataPerStato.class);
		// Parametri di ricerca
		req.setBilancio(getBilancio());
		req.setRequestRicerca(reqRicerca);
		
		req.getRequestRicerca().setUidPredocumentiDaFiltrare(getListaUid());
		//SIAC-6780
		req.setContoCorrente(impostaEntitaFacoltativa(getContoCorrente()));
		
		return req;
	}
	
	
	/**
	 * Crea una request per il servizio di {@link DefiniscePreDocumentoEntrata}.
	 * @param ricercaSinteticaPreDocumentoEntrata la request della ricerca sintetica, nel caso in cui siano da inviare tutti i preDocumenti
	 * @return la request creata
	 */
	public DefiniscePreDocumentoEntrata creaRequestDefiniscePreDocumentoEntrata(RicercaSinteticaPreDocumentoEntrata ricercaSinteticaPreDocumentoEntrata) {
		DefiniscePreDocumentoEntrata request = creaRequest(DefiniscePreDocumentoEntrata.class);
		
		request.setDataOra(new Date());
		request.setRichiedente(getRichiedente());
		
		List<PreDocumentoEntrata> listaPreDoc = popolaListaPreDocByUid(getListaUid());
		
		request.setPreDocumentiEntrata(listaPreDoc);
		request.setBilancio(getBilancio());
		
		if(Boolean.TRUE.equals(isInviaTutti())) {
			request.setRicercaSinteticaPreDocumentoEntrata(ricercaSinteticaPreDocumentoEntrata);
		}
		
		return request;
	}

	/**
	 * @param list 
	 * @return
	 */
	private List<PreDocumentoEntrata> popolaListaPreDocByUid(List<Integer> list) {
		List<PreDocumentoEntrata> listaPreDoc = new ArrayList<PreDocumentoEntrata>();
		
		for (Integer integer : list) {
			PreDocumentoEntrata preDoc = new PreDocumentoEntrata();
			preDoc.setUid(integer);
			listaPreDoc.add(preDoc);
		}
		return listaPreDoc;
	}

}

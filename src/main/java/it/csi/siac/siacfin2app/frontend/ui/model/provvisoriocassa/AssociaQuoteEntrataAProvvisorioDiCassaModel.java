/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.provvisoriocassa;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AssociaProvvisorioQuoteEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuotePerProvvisorioEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTipoDocumento;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;
import it.csi.siac.siacfin2ser.model.TipoFamigliaDocumento;

/**
 * Classe di model per la ricerca sintetica del provvisorio di cassa.
 * 
 * @author Valentina
 * @version 1.0.0 - 15/03/2016
 */
public class AssociaQuoteEntrataAProvvisorioDiCassaModel extends AssociaQuoteAProvvisorioDiCassaModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 7560062423915710216L;
	
	private BigDecimal totaleSubdocumentiEntrata;
	
	/** Cotruttore vuoto di default */
	public AssociaQuoteEntrataAProvvisorioDiCassaModel() {
		super();
		setTitolo("Associa quote entrata");
	}
	
	/**
	 * @return the totaleSubdocumentiEntrata
	 */
	public BigDecimal getTotaleSubdocumentiEntrata() {
		return totaleSubdocumentiEntrata;
	}

	/**
	 * @param totaleSubdocumentiEntrata the totaleSubdocumentiEntrata to set
	 */
	public void setTotaleSubdocumentiEntrata(BigDecimal totaleSubdocumentiEntrata) {
		this.totaleSubdocumentiEntrata = totaleSubdocumentiEntrata;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaTipoDocumento}.
	 * 
	 * @return la request creata
	 */
	public RicercaTipoDocumento creaRequestRicercaTipoDocumento() {
		RicercaTipoDocumento request = creaRequest(RicercaTipoDocumento.class);
		request.setEnte(getEnte());
		request.setTipoFamDoc(TipoFamigliaDocumento.ENTRATA);
		return request;
	}

	/**
	 * Crea una request per il servizio di {@link RicercaQuotePerProvvisorioEntrata}.
	 * 
	 * @return la request creata
	 */
	public RicercaQuotePerProvvisorioEntrata creaRequestRicercaQuotePerProvvisorioEntrata() {
		RicercaQuotePerProvvisorioEntrata request = creaRequest(RicercaQuotePerProvvisorioEntrata.class);
		request.setParametriPaginazione(new ParametriPaginazione(0, AssociaQuoteAProvvisorioDiCassaModel.ELEMENTI_PER_PAGINA_QUOTA));
		request.setAnnoDocumento(getAnnoDocumento());
		request.setAnnoMovimento(getAnnoMovimento());
		request.setDataEmissioneDocumento(getDataEmissioneDocumento());
		request.setNumeroDocumento(getNumeroDocumento());
		request.setNumeroMovimento(getNumeroMovimento());
		request.setNumeroQuota(getNumeroSubdocumento());
		request.setSoggetto(getSoggetto());
		request.setTipoDocumento(getTipoDocumento());
		request.setNumeroElenco(getNumeroElenco());
		request.setAnnoElenco(getAnnoElenco());
		request.setFlgEscludiSubDocCollegati(true);
		
		return request;
	}

	/**
	 * Crea una request per il servizio di {@link AssociaProvvisorioQuoteEntrata}.
	 * 
	 * @return la request creata
	 */
	public AssociaProvvisorioQuoteEntrata creaRequestAssociaProvvisorioQuoteEntrata() {
		AssociaProvvisorioQuoteEntrata request = creaRequest(AssociaProvvisorioQuoteEntrata.class);
		List<SubdocumentoEntrata> listaQuote = new ArrayList<SubdocumentoEntrata>();
		for(Integer uid : getListaUidSubdocumenti()){
			SubdocumentoEntrata subdoc = new SubdocumentoEntrata();
			subdoc.setUid(uid);
			listaQuote.add(subdoc);
		}
		request.setListaQuote(listaQuote);
		request.setProvvisorioDiCassa(getProvvisorio());
		return request;
	}
}

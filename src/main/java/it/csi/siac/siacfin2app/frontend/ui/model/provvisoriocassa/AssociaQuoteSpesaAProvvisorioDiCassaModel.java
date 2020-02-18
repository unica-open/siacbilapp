/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.provvisoriocassa;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AssociaProvvisorioQuoteSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuotePerProvvisorioSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTipoDocumento;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfin2ser.model.TipoFamigliaDocumento;

/**
 * Classe di model per la ricerca sintetica del provvisorio di cassa.
 * 
 * @author Valentina
 * @version 1.0.0 - 15/03/2016
 */
public class AssociaQuoteSpesaAProvvisorioDiCassaModel extends AssociaQuoteAProvvisorioDiCassaModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -1908691701626233116L;

	private BigDecimal totaleSubdocumentiSpesa;
	
	
	/** Cotruttore vuoto di default */
	public AssociaQuoteSpesaAProvvisorioDiCassaModel() {
		super();
		setTitolo("Associa quote spesa");
	}
	
	/**
	 * @return the totaleSubdocumentiSpesa
	 */
	public BigDecimal getTotaleSubdocumentiSpesa() {
		return totaleSubdocumentiSpesa;
	}

	/**
	 * @param totaleSubdocumentiSpesa the totaleSubdocumentiSpesa to set
	 */
	public void setTotaleSubdocumentiSpesa(BigDecimal totaleSubdocumentiSpesa) {
		this.totaleSubdocumentiSpesa = totaleSubdocumentiSpesa;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaTipoDocumento}.
	 * 
	 * @return la request creata
	 */
	public RicercaTipoDocumento creaRequestRicercaTipoDocumento() {
		RicercaTipoDocumento request = creaRequest(RicercaTipoDocumento.class);
		request.setEnte(getEnte());
		request.setTipoFamDoc(TipoFamigliaDocumento.SPESA);
		return request;
	}

	/**
	 * Crea una request per il servizio di {@link RicercaQuotePerProvvisorioSpesa}.
	 * 
	 * @return la request creata
	 */
	public RicercaQuotePerProvvisorioSpesa creaRequestRicercaQuotePerProvvisorioSpesa() {
		RicercaQuotePerProvvisorioSpesa request = creaRequest(RicercaQuotePerProvvisorioSpesa.class);
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
		return request;
	}

	/**
	 * Crea una request per il servizio di {@link AssociaProvvisorioQuoteSpesa}.
	 * 
	 * @return la request creata
	 */
	public AssociaProvvisorioQuoteSpesa creaRequestAssociaProvvisorioQuoteSpesa() {
		AssociaProvvisorioQuoteSpesa request = creaRequest(AssociaProvvisorioQuoteSpesa.class);
		List<SubdocumentoSpesa> listaQuote = new ArrayList<SubdocumentoSpesa>();
		for(Integer uid : getListaUidSubdocumenti()){
			SubdocumentoSpesa subdoc = new SubdocumentoSpesa();
			subdoc.setUid(uid);
			listaQuote.add(subdoc);
		}
		request.setListaQuote(listaQuote);
		request.setProvvisorioDiCassa(getProvvisorio());
		return request;
	}
}

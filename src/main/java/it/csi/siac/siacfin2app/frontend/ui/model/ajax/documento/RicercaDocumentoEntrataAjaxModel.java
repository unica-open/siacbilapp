/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.ajax.documento;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siacbilapp.frontend.ui.util.format.FormatUtils;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siacfin2app.frontend.ui.model.documento.RicercaDocumentoModel;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaQuoteByDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaModulareDocumentoEntrata;
import it.csi.siac.siacfin2ser.model.Documento;
import it.csi.siac.siacfin2ser.model.DocumentoEntrata;
import it.csi.siac.siacfin2ser.model.DocumentoEntrataModelDetail;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;
import it.csi.siac.siacfin2ser.model.TipoDocumento;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentoPerChiave;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.ric.RicercaAccertamentoK;


/**
 * Classe di model per la gestione della ricerca della lista di fatture
 * 
 * @author Paggio Simona
 * @version 1.0.0 - 10/02/2015
 *
 */
public class RicercaDocumentoEntrataAjaxModel extends RicercaDocumentoModel {

	/** Per la serializzazione */
	private static final long serialVersionUID = -387011288099500399L;
	
	private DocumentoEntrata documentoEntrata;
	private Accertamento accertamento;
	private Boolean collegatoCEC;
	private Boolean contabilizzaGenPcc;
	private BigDecimal importoTotale;
	
	private List<SubdocumentoEntrata> listaSubdocumentoEntrata = new ArrayList<SubdocumentoEntrata>();
	
	/**
	 * @return the documentoEntrata
	 */
	public DocumentoEntrata getDocumentoEntrata() {
		return documentoEntrata;
	}

	/**
	 * @param documentoEntrata the documentoEntrata to set
	 */
	public void setDocumentoEntrata(DocumentoEntrata documentoEntrata) {
		this.documentoEntrata = documentoEntrata;
	}

	

	/**
	 * @return the accertamento
	 */
	public final Accertamento getAccertamento() {
		return accertamento;
	}

	/**
	 * @param accertamento the accertamento to set
	 */
	public final void setAccertamento(Accertamento accertamento) {
		this.accertamento = accertamento;
	}

	/**
	 * @return the collegatoCEC
	 */
	public Boolean getCollegatoCEC() {
		return collegatoCEC;
	}

	/**
	 * @param collegatoCEC the collegatoCEC to set
	 */
	public void setCollegatoCEC(Boolean collegatoCEC) {
		this.collegatoCEC = collegatoCEC;
	}

	/**
	 * @return the contabilizzaGenPcc
	 */
	public Boolean getContabilizzaGenPcc() {
		return contabilizzaGenPcc;
	}

	/**
	 * @param contabilizzaGenPcc the contabilizzaGenPcc to set
	 */
	public void setContabilizzaGenPcc(Boolean contabilizzaGenPcc) {
		this.contabilizzaGenPcc = contabilizzaGenPcc;
	}

	/**
	 * @return the listaSubdocumentoEntrata
	 */
	public List<SubdocumentoEntrata> getListaSubdocumentoEntrata() {
		return listaSubdocumentoEntrata;
	}

	/**
	 * @param listaSubdocumentoEntrata the listaSubdocumentoEntrata to set
	 */
	public void setListaSubdocumentoEntrata(List<SubdocumentoEntrata> listaSubdocumentoEntrata) {
		this.listaSubdocumentoEntrata = listaSubdocumentoEntrata != null ? listaSubdocumentoEntrata : new ArrayList<SubdocumentoEntrata>();
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

	@Override
	public String componiStringaRiepilogo(Documento<?, ?> documento, List<TipoDocumento> listaTipoDoc, List<TipoAtto> listaTipoAtto,
			List<StrutturaAmministrativoContabile> listaStrutturaAmministrativoContabile) {
		List<String> components = new ArrayList<String>();
		
		components.add(super.componiStringaRiepilogo(documento, listaTipoDoc, listaTipoAtto, listaStrutturaAmministrativoContabile));
		if(StringUtils.isNotBlank(getFlagIva())) {
			components.add("Con capitolo rilevante IVA: " + FormatUtils.formatBoolean("S".equals(getFlagIva()), "Si", "No"));
		}
		return StringUtils.join(components, " - ");
	}
	
	/* **** Requests **** */
	
	/**
	 * Crea una request per il servizio di Ricerca Sintetica Documento Entrata
	 * @deprecated usare {@link #creaRequestRicercaSinteticaModulareDocumentoEntrata()}
	 * @return la request creata
	 */
	@Deprecated
	public RicercaSinteticaDocumentoEntrata creaRequestRicercaSinteticaDocumentoEntrata() {
		RicercaSinteticaDocumentoEntrata request = creaRequest(RicercaSinteticaDocumentoEntrata.class);
		
		getDocumentoEntrata().setEnte(getEnte());
		
		request.setDocumentoEntrata(getDocumentoEntrata());
		request.setContabilizzaGenPcc(getContabilizzaGenPcc());
		
		if(checkPresenzaIdEntita(getAccertamento())) {
			request.setAccertamento(getAccertamento());
		}
		if(Boolean.TRUE.equals(getElencoPresente())) {
			request.setElencoDocumenti(getElencoDocumenti());
		}
		
		request.setParametriPaginazione(creaParametriPaginazione());
		return request;
	}
	
	/**
	 * Crea una request per il servizio di Ricerca Sintetica Modulare  delDocumento Entrata
	 * @return la request creata
	 */
	public RicercaSinteticaModulareDocumentoEntrata creaRequestRicercaSinteticaModulareDocumentoEntrata() {
		RicercaSinteticaModulareDocumentoEntrata request = creaRequest(RicercaSinteticaModulareDocumentoEntrata.class);
		getDocumentoEntrata().setEnte(getEnte());
		
		request.setDocumentoEntrata(getDocumentoEntrata());
		request.setContabilizzaGenPcc(getContabilizzaGenPcc());
		
		if(checkPresenzaIdEntita(getAccertamento())) {
			request.setAccertamento(getAccertamento());
		}
		if(Boolean.TRUE.equals(getElencoPresente())) {
			request.setElencoDocumenti(getElencoDocumenti());
		}
		
		request.setDocumentoEntrataModelDetails(new DocumentoEntrataModelDetail[] {DocumentoEntrataModelDetail.TipoDocumento, DocumentoEntrataModelDetail.Stato});
		
		request.setParametriPaginazione(creaParametriPaginazione());
		
		return request;
	}

	
	/**
	 * Crea una request per il servizio di {@link RicercaSinteticaDocumentoEntrata}.
	 * 
	 * @return la request creata
	 */
	public RicercaSinteticaDocumentoEntrata creaRequestRicercaDocumentoEntrata() {
		RicercaSinteticaDocumentoEntrata request = creaRequest(RicercaSinteticaDocumentoEntrata.class);
		
		getDocumentoEntrata().setEnte(getEnte());
		getDocumentoEntrata().setSoggetto(getSoggetto());
		request.setDocumentoEntrata(getDocumentoEntrata());
		request.setContabilizzaGenPcc(getContabilizzaGenPcc());
		
		request.setParametriPaginazione(creaParametriPaginazione());
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaAccertamentoPerChiave}.
	 * 
	 * @return la request creata
	 */
	public RicercaAccertamentoPerChiave creaRequestRicercaAccertamento(){
		RicercaAccertamentoPerChiave request = creaRequest(RicercaAccertamentoPerChiave.class);
		
		RicercaAccertamentoK parametroRicercaAccertamentoK = new RicercaAccertamentoK();
		if(accertamento != null) {
			parametroRicercaAccertamentoK.setAnnoAccertamento(accertamento.getAnnoMovimento());
			parametroRicercaAccertamentoK.setNumeroAccertamento(accertamento.getNumeroBigDecimal());
			parametroRicercaAccertamentoK.setAnnoEsercizio(getAnnoEsercizioInt());
		}
		
		request.setDataOra(new Date());
		request.setRichiedente(getRichiedente());
		request.setEnte(getEnte());
		request.setpRicercaAccertamentoK(parametroRicercaAccertamentoK);
				
		return request;
	}

	/**
	 * Crea una request per il servizio di {@link RicercaQuoteByDocumentoEntrata}.
	 * 
	 * @return la request creata
	 */
	public RicercaQuoteByDocumentoEntrata creaRequestRicercaQuoteByDocumentoEntrata() {
		RicercaQuoteByDocumentoEntrata request = creaRequest(RicercaQuoteByDocumentoEntrata.class);
		
		request.setDocumentoEntrata(getDocumentoEntrata());
		
		return request;
	}

	@Override
	protected void componiStringaRiepilogoMovimento(List<String> components) {
		List<String> subComponents = new ArrayList<String>();
		
		if(getAccertamento().getAnnoMovimento() != 0) {
			subComponents.add(getAccertamento().getAnnoMovimento() + "");
		}
		if(getAccertamento().getNumeroBigDecimal() != null) {
			subComponents.add(getAccertamento().getNumeroBigDecimal().toPlainString());
		}
		
		components.add("Movimento: " + StringUtils.join(subComponents, "/"));
	}

	


}

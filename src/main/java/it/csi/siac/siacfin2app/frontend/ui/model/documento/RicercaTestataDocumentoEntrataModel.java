/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.documento;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siacbilapp.frontend.ui.util.format.FormatUtils;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaDocumentoEntrata;
import it.csi.siac.siacfin2ser.model.Documento;
import it.csi.siac.siacfin2ser.model.DocumentoEntrata;
import it.csi.siac.siacfin2ser.model.TipoDocumento;
import it.csi.siac.siacfinser.model.Accertamento;

/**
 * Classe di model per la ricerca della testata del documento di entrata.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 10/07/2014
 */
public class RicercaTestataDocumentoEntrataModel extends RicercaDocumentoModel {
	
	/** Per la serialiazzazione */
	private static final long serialVersionUID = -1462377452810821464L;
	
	private DocumentoEntrata documento;
	private Accertamento accertamento;
	
	/** Costruttore vuoto di default */
	public RicercaTestataDocumentoEntrataModel() {
		setTitolo("Ricerca Documento iva Entrata");
		setLabelEntrataSpesa("Entrata");
		setLabelRicercaEntrataSpesa("Ricerca Documenti iva entrata");
		setLabelRilevanteIvaEntrataSpesa("Rilevante IVA");
	}

	/**
	 * @return the documento
	 */
	public DocumentoEntrata getDocumento() {
		return documento;
	}

	/**
	 * @param documento the documento to set
	 */
	public void setDocumento(DocumentoEntrata documento) {
		this.documento = documento;
	}

	/**
	 * @return the accertamento
	 */
	public Accertamento getAccertamento() {
		return accertamento;
	}

	/**
	 * @param accertamento the accertamento to set
	 */
	public void setAccertamento(Accertamento accertamento) {
		this.accertamento = accertamento;
	}

	/* **** Request **** */
	/**
	 * Crea una request per il servizio di {@link RicercaSinteticaDocumentoEntrata}.
	 * 
	 * @return la request creata
	 */
	public RicercaSinteticaDocumentoEntrata creaRequestRicercaSinteticaDocumentoEntrata() {
		RicercaSinteticaDocumentoEntrata request = creaRequest(RicercaSinteticaDocumentoEntrata.class);
		
		getDocumento().setEnte(getEnte());
		
		injettaSoggetto();
		
		request.setDocumentoEntrata(getDocumento());
		request.setRilevanteIva(FormatUtils.parseBooleanSN(getFlagIva()));
		
		if(checkPresenzaIdEntita(getAttoAmministrativo())) {
			request.setAttoAmministrativo(getAttoAmministrativo());
		}
		
		request.setParametriPaginazione(creaParametriPaginazione());
		return request;
	}
	
	
	/**
	 * Compone una stringa con i parametri di ricerca impostati.
	 * 
	 * @param documento                             il documento
	 * @param listaTipoDoc                          la lista dei tipi di documento
	 * @param listaTipoAtto                         la lista dei tipi di atto
	 * @param listaStrutturaAmministrativoContabile la lista delle strutture amministativo contabili
	 * 
	 * @return la stringa di riepilogo 
	 */
	@Override
	public String componiStringaRiepilogo(Documento<?, ?> documento, List<TipoDocumento> listaTipoDoc, List<TipoAtto> listaTipoAtto, List<StrutturaAmministrativoContabile> listaStrutturaAmministrativoContabile) {
		List<String> components = new ArrayList<String>();
		
		components.add(super.componiStringaRiepilogo(documento, listaTipoDoc, listaTipoAtto, listaStrutturaAmministrativoContabile));
		if(StringUtils.isNotBlank(getFlagIva())) {
			components.add("Rilevante IVA: " + FormatUtils.formatBoolean("S".equals(getFlagIva()), "Si", "No"));
		}
		return StringUtils.join(components, " - ");
	}
	
	private void injettaSoggetto() {
		if(!getSoggettoPresente()) {
			return;
		}
		documento.setSoggetto(getSoggetto());
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

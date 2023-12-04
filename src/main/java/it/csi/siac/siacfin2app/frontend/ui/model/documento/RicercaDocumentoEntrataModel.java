/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.documento;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siacbilapp.frontend.ui.util.format.FormatUtils;
import it.csi.siac.siacbilser.model.ImportiCapitoloEnum;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaModulareDocumentoEntrata;
import it.csi.siac.siacfin2ser.model.Documento;
import it.csi.siac.siacfin2ser.model.DocumentoEntrata;
import it.csi.siac.siacfin2ser.model.DocumentoEntrataModelDetail;
import it.csi.siac.siacfin2ser.model.PreDocumentoEntrata;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;
import it.csi.siac.siacfin2ser.model.TipoDocumento;
import it.csi.siac.siacfinser.frontend.webservice.msg.DatiOpzionaliCapitoli;
import it.csi.siac.siacfinser.frontend.webservice.msg.DatiOpzionaliElencoSubTuttiConSoloGliIds;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentoPerChiaveOttimizzato;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.ric.RicercaAccertamentoK;

/**
  *  Classe di model per la ricerca del documento di entrata.
  * 
  * @author Osorio Alessandra
  * @version 1.0.0 - 10/03/2014
  *
  */
public class RicercaDocumentoEntrataModel extends RicercaDocumentoModel {
			
	/** Per la serialiazzazione */
	private static final long serialVersionUID = 7033998987854718167L;
	
	private DocumentoEntrata documento;
	
	private Accertamento accertamento;
	
	private List<SubdocumentoEntrata> listaSubDocRicerca = new ArrayList<SubdocumentoEntrata>();
	
	//SIAC-6780
		private PreDocumentoEntrata predocumento;
	
	/** Costruttore vuoto di default */
	public RicercaDocumentoEntrataModel() {
		setTitolo("Ricerca Documento Entrata");
		setLabelEntrataSpesa("Entrata");
		setLabelRicercaEntrataSpesa("Ricerca Documenti di entrata");
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

	/**
	 * @return the listaSubDocRicerca
	 */
	public List<SubdocumentoEntrata> getListaSubDocRicerca() {
		return listaSubDocRicerca;
	}

	/**
	 * @param listaSubDocRicerca the listaSubDocRicerca to set
	 */
	public void setListaSubDocRicerca(List<SubdocumentoEntrata> listaSubDocRicerca) {
		this.listaSubDocRicerca = listaSubDocRicerca;
	}
	
	/**
	 * @return the predocumento
	 */
	public PreDocumentoEntrata getPredocumento() {
		return predocumento;
	}

	/**
	 * @param predocumento the predocumento to set
	 */
	public void setPredocumento(PreDocumentoEntrata predocumento) {
		this.predocumento = predocumento;
	}

	/**
	 * Crea una request per il servizio di Ricerca Sintetica Modulare Documento Entrata
	 * 
	 * @return la request creata
	 */
	public RicercaSinteticaModulareDocumentoEntrata creaRequestRicercaSinteticaModulareDocumentoEntrata() {
		RicercaSinteticaModulareDocumentoEntrata request = creaRequest(RicercaSinteticaModulareDocumentoEntrata.class);
		
		documento.setEnte(getEnte());
		
		injettaSoggetto();
		
		request.setDocumentoEntrata(documento);
		request.setRilevanteIva(FormatUtils.parseBooleanSN(getFlagIva()));
		//SIAC-5660
		//if(checkPresenzaIdEntita(getAttoAmministrativo())) {
			request.setAttoAmministrativo(getAttoAmministrativo());
		//}
		if(checkPresenzaIdEntita(getAccertamento())) {
			request.setAccertamento(getAccertamento());
		}
		if(Boolean.TRUE.equals(getElencoPresente())) {
			request.setElencoDocumenti(getElencoDocumenti());
		}
		
		if(getPredocumento() != null && StringUtils.isNotBlank(getPredocumento().getNumero())) {
			request.setPreDocumentoEntrata(getPredocumento());
		}
		
		request.setParametriPaginazione(creaParametriPaginazione());
		request.setDocumentoEntrataModelDetails(
				DocumentoEntrataModelDetail.Sogg,
				DocumentoEntrataModelDetail.Stato,
				DocumentoEntrataModelDetail.TipoDocumento);
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaAccertamentoPerChiaveOttimizzato}.
	 * 
	 * @return la request creata
	 */
	public RicercaAccertamentoPerChiaveOttimizzato creaRequestRicercaAccertamentoPerChiaveOttimizzato(){
		RicercaAccertamentoPerChiaveOttimizzato request = creaPaginazioneRequest(RicercaAccertamentoPerChiaveOttimizzato.class);
		
		if(accertamento != null) {
			RicercaAccertamentoK parametroRicercaAccertamentoK = new RicercaAccertamentoK();
			parametroRicercaAccertamentoK.setAnnoAccertamento(accertamento.getAnnoMovimento());
			parametroRicercaAccertamentoK.setNumeroAccertamento(accertamento.getNumeroBigDecimal());
			parametroRicercaAccertamentoK.setAnnoEsercizio(getAnnoEsercizioInt());
			request.setpRicercaAccertamentoK(parametroRicercaAccertamentoK);
		}
		
		request.setEnte(getEnte());
		request.setCaricaSub(false);
		request.setSubPaginati(true);
		
		DatiOpzionaliElencoSubTuttiConSoloGliIds datiOpzionaliElencoSubTuttiConSoloGliIds = new DatiOpzionaliElencoSubTuttiConSoloGliIds();
		datiOpzionaliElencoSubTuttiConSoloGliIds.setEscludiAnnullati(true);
		request.setDatiOpzionaliElencoSubTuttiConSoloGliIds(datiOpzionaliElencoSubTuttiConSoloGliIds);
		
		// Non richiedo NESSUN importo derivato.
		DatiOpzionaliCapitoli datiOpzionaliCapitoli = new DatiOpzionaliCapitoli();
		datiOpzionaliCapitoli.setImportiDerivatiRichiesti(EnumSet.noneOf(ImportiCapitoloEnum.class));

		request.setDatiOpzionaliCapitoli(datiOpzionaliCapitoli);
		
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

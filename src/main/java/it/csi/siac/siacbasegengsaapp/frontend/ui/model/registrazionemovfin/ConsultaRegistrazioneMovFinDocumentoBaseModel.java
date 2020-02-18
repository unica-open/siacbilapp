/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.model.registrazionemovfin;


import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.registrazionemovfin.consultazione.ConsultaRegistrazioneMovFinDocumentoHelper;
import it.csi.siac.siacbilapp.frontend.ui.util.format.FormatUtils;
import it.csi.siac.siacfin2ser.model.Documento;
import it.csi.siac.siacfin2ser.model.Subdocumento;
import it.csi.siac.siacfin2ser.model.SubdocumentoIva;

/**
 * Consultazione della registrazione per il Documento. Classe base
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 07/10/2015
 * @param <D>  la tipizzazione del documento
 * @param <S>  la tipizzazione del subdocumento
 * @param <SI> la tipizzazione del subdocumento iva
 * @param <H> la tipizzazione dell'helper
 *
 */
public abstract class ConsultaRegistrazioneMovFinDocumentoBaseModel<D extends Documento<S, SI>, S extends Subdocumento<D, SI>, SI extends SubdocumentoIva<D, S, SI>,
		H extends ConsultaRegistrazioneMovFinDocumentoHelper<D, S, SI>> extends ConsultaRegistrazioneMovFinBaseModel<D, H> {

	/** Per la serializzazione */
	private static final long serialVersionUID = 3615972704305354519L;
	
	private Integer uidDocumento;
	private D documento;

	/**
	 * @return the uidDocumento
	 */
	public Integer getUidDocumento() {
		return uidDocumento;
	}

	/**
	 * @param uidDocumento the uidDocumento to set
	 */
	public void setUidDocumento(Integer uidDocumento) {
		this.uidDocumento = uidDocumento;
	}

	/**
	 * @return the documento
	 */
	public D getDocumento() {
		return this.documento;
	}

	/**
	 * @param documento the documento to set
	 */
	public void setDocumento(D documento) {
		this.documento = documento;
	}

	@Override
	public String getStato() {
		if(consultazioneHelper.getDocumento() == null || consultazioneHelper.getDocumento().getStatoOperativoDocumento() == null) {
			return "";
		}
		return new StringBuilder()
			.append("Stato documento: ")
			.append(consultazioneHelper.getDocumento().getStatoOperativoDocumento().getDescrizione())
			.append(" dal ")
			.append(FormatUtils.formatDate(consultazioneHelper.getDocumento().getDataInizioValiditaStato()))
			.toString();
	}

	@Override
	public String getIntestazione() {
		if(consultazioneHelper.getDocumento() == null || consultazioneHelper.getDocumento().getTipoDocumento() == null) {
			return "";
		}
		return new StringBuilder()
			.append(consultazioneHelper.getDocumento().getTipoDocumento().getCodice())
			.append(" - ")
			.append(consultazioneHelper.getDocumento().getTipoDocumento().getDescrizione())
			.append(" - ")
			.append(consultazioneHelper.getDocumento().getAnno())
			.append(" - ")
			.append(consultazioneHelper.getDocumento().getNumero())
			.toString();
	}
	
	@Override
	public String getDatiCreazioneModifica() {
		return consultazioneHelper.getDatiCreazioneModifica();
	}
	
}

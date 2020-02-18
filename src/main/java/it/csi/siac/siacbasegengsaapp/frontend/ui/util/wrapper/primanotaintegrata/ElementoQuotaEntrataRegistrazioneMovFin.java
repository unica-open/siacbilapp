/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.primanotaintegrata;

import it.csi.siac.siacfin2ser.model.DocumentoEntrata;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;
import it.csi.siac.siacgenser.model.RegistrazioneMovFin;

/**
 * Elemento delle scritture corrispondenti alla riga rappresentante il singolo movimentoEP per la nota integrata. Relativo alla spesa
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 15/05/2015
 * 
 */
public class ElementoQuotaEntrataRegistrazioneMovFin extends ElementoQuotaRegistrazioneMovFin<DocumentoEntrata, SubdocumentoEntrata> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 7872492960345198349L;

	/**
	 * Costruttore di wrap.
	 * 
	 * @param subdocumento        il subdocumento da impostare
	 * @param registrazioneMovFin la registrazione da impostare
	 */
	public ElementoQuotaEntrataRegistrazioneMovFin(SubdocumentoEntrata subdocumento, RegistrazioneMovFin registrazioneMovFin) {
		super(subdocumento, registrazioneMovFin);
	}
	
	/**
	 * Costruttore di wrap per le note credito
	 * 
	 * @param notaCredito        la nota credito associata al documento
	 * @param subdocumento        il subdocumento da impostare
	 * @param registrazioneMovFin la registrazione da impostare
	 */
	public ElementoQuotaEntrataRegistrazioneMovFin(DocumentoEntrata notaCredito, SubdocumentoEntrata subdocumento, RegistrazioneMovFin registrazioneMovFin) {
		super(notaCredito, subdocumento, registrazioneMovFin);
	}
}

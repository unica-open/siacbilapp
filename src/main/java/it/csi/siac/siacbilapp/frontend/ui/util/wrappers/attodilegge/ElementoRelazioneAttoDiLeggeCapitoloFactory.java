/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.util.wrappers.attodilegge;

import java.io.Serializable;

import it.csi.siac.siacbilapp.frontend.ui.util.format.FormatUtils;
import it.csi.siac.siacbilser.model.AttoDiLeggeCapitolo;

/**
 * Factory per il wrapping delle relazioni atti di legge.
 * 
 * @author Luciano Gallo
 * @version 1.0.0 18/09/2013
 *
 */
public final class ElementoRelazioneAttoDiLeggeCapitoloFactory implements Serializable {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -2207836856773579048L;
	
	/** Stringa di utilit&agrave; per i campi non presenti */
	// Un tempo era "undefined"
	private static final String UNDEFINED = "NON DEFINITO";

	/** Non permettere l'instanziazione della classe */
	private ElementoRelazioneAttoDiLeggeCapitoloFactory() {
	}
	
	/**
	 * Wrapper per la relazione tra atto di legge e capitolo.
	 * 
	 * @param relazione da wrappare
	 * 
	 * @return il wrapper
	 */
	public static ElementoRelazioneAttoDiLeggeCapitolo getInstance(AttoDiLeggeCapitolo relazione) {
		if(relazione == null) {
			return null;
		}
		ElementoRelazioneAttoDiLeggeCapitolo wrapper = new ElementoRelazioneAttoDiLeggeCapitolo();
		
		// Campi da injettare
		int uid;
		String tipoAtto;
		String anno;
		String numero;
		String articolo;
		String comma;
		String punto;
		String gerarchia;
		String descrizione;
		String dataInizioFinanziamento;
		String dataFineFinanziamento;
		int uidAttoDiLegge;

		
		// Valorizzazione dei campi
		uid = relazione.getUid();
		uidAttoDiLegge = relazione.getAttoDiLegge().getUid();
		anno = relazione.getAttoDiLegge().getAnno() + "";
		numero = relazione.getAttoDiLegge().getNumero() + "";
		if (relazione.getAttoDiLegge().getTipoAtto() != null) {
			tipoAtto = relazione.getAttoDiLegge().getTipoAtto().getCodice()+"-"+
					relazione.getAttoDiLegge().getTipoAtto().getDescrizione();
		} else {
			tipoAtto = UNDEFINED;
		}
		
		articolo = relazione.getAttoDiLegge().getArticolo();
		comma = relazione.getAttoDiLegge().getComma();
		punto = relazione.getAttoDiLegge().getPunto();
		
		gerarchia = relazione.getGerarchia();
		descrizione = relazione.getDescrizione();
		
		dataInizioFinanziamento = FormatUtils.formatDate(relazione.getDataInizioFinanz());
		dataFineFinanziamento = FormatUtils.formatDate(relazione.getDataFineFinanz());
		
		// Injezione dei campi
		wrapper.setUid(uid);
		wrapper.setAnno(anno);
		wrapper.setNumero(numero);
		wrapper.setTipoAtto(tipoAtto);
		wrapper.setArticolo(articolo);
		wrapper.setComma(comma);
		wrapper.setPunto(punto);
		wrapper.setDescrizione(descrizione);
		wrapper.setGerarchia(gerarchia);
		wrapper.setDataInizioFinanziamento(dataInizioFinanziamento);
		wrapper.setDataFineFinanziamento(dataFineFinanziamento);
		wrapper.setUidAttoDiLegge(uidAttoDiLegge);
		
		
		return wrapper;
	}
	

}

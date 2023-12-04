/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.util.wrappers.provvedimento;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siaccommon.util.collections.CollectionUtil;

/**
 * Factory per il wrapping dei provvedimenti.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 18/09/2013
 *
 */
public final class ElementoProvvedimentoFactory {
	
	/** Stringa di utilit&agrave; per i campi non presenti */
	// Un tempo era "undefined"
	private static final String UNDEFINED = "";
	
	/** Non permettere l'instanziazione della classe */
	private ElementoProvvedimentoFactory() {
	}
	
	/**
	 * Wrapper per il Provvedimento.
	 * 
	 * @param provvedimento il provvedimento da wrappare
	 * 
	 * @return il wrapper
	 */
	public static ElementoProvvedimento getInstance(AttoAmministrativo provvedimento) {
		if(provvedimento == null) {
			return null;
		}
		ElementoProvvedimento wrapper = new ElementoProvvedimento();
		
		// Campi da injettare
		int uid;
		String anno;
		String numero;
		String tipo;
		String oggetto;
		String strutturaAmministrativoContabile;
		String stato;
		int uidTipo;
		int uidStrutturaAmministrativoContabile;
		String codiceTipo;
		
		
		// Valorizzazione dei campi
		uid = provvedimento.getUid();
		anno = provvedimento.getAnno() + "";
		numero = provvedimento.getNumero() + "";
		tipo = provvedimento.getTipoAtto().getDescrizione();
		codiceTipo = provvedimento.getTipoAtto().getCodice();
		oggetto = provvedimento.getOggetto();
		stato = provvedimento.getStatoOperativo();
		strutturaAmministrativoContabile = extractSAC(provvedimento);
	
		uidTipo = provvedimento.getTipoAtto().getUid();
		uidStrutturaAmministrativoContabile = 
				(provvedimento.getStrutturaAmmContabile() == null ? 
						0 : 
						provvedimento.getStrutturaAmmContabile().getUid());
		
		// Injezione dei campi
		wrapper.setUid(uid);
		wrapper.setAnno(anno);
		wrapper.setNumero(numero);
		wrapper.setTipo(tipo);
		wrapper.setOggetto(oggetto);
		wrapper.setStrutturaAmministrativoContabile(strutturaAmministrativoContabile);
		wrapper.setStato(stato);
		wrapper.setUidTipo(uidTipo);
		wrapper.setUidStrutturaAmministrativoContabile(uidStrutturaAmministrativoContabile);
		wrapper.setCodiceTipo(codiceTipo);
		
		return wrapper;
	}

	/**
	 * Estrazione dei dati della SAC
	 * @param provvedimento il provvedimento i cui dati della SAC devono essere ottenuti
	 * @return i dati della SAC
	 */
	private static String extractSAC(AttoAmministrativo provvedimento) {
		if(provvedimento.getStrutturaAmmContabile() == null) {
			return UNDEFINED;
		}
		return CollectionUtil.join("-",
				provvedimento.getStrutturaAmmContabile().getCodice(),
				provvedimento.getStrutturaAmmContabile().getDescrizione());
	}
	
}

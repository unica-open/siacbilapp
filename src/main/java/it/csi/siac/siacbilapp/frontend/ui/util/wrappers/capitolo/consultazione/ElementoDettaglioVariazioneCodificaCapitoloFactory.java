/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.consultazione;

import java.util.List;

import it.csi.siac.siacbilapp.frontend.ui.util.format.FormatUtils;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.model.DettaglioVariazioneCodificaCapitolo;
import it.csi.siac.siacbilser.model.VariazioneCodificaCapitolo;
import it.csi.siac.siaccorser.model.ClassificatoreGerarchico;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;


/**
 * Classe di factory per l'ElementoCapitoloCodifiche.
 * 
 * @author Elisa Chiari
 * @version 1.0.0 06/10/2016
 *
 */
public final class ElementoDettaglioVariazioneCodificaCapitoloFactory {
	
	
	/** Non permettere l'instanziazione della classe */
	private ElementoDettaglioVariazioneCodificaCapitoloFactory() {
	}
	
	
	/**
	 * Ottiene un'istanza a partire dal dettaglio della variazione.
	 * 
	 * @param dati il dettaglio da wrappare
	 * 
	 * @return il wrapper ottenuto
	 */
	public static ElementoDettaglioVariazioneCodificaCapitolo getInstance(DettaglioVariazioneCodificaCapitolo dati) {
		ElementoDettaglioVariazioneCodificaCapitolo result = new ElementoDettaglioVariazioneCodificaCapitolo();
		//campi opzionali
		Capitolo<?,?> capitoloPrecedente = dati.getCapitoloPrecedente();
		Capitolo<?,?> capitoloVariazione = dati.getCapitolo();
		
		result.setDescrizioneCapitoloPreVariazione(ottieniStringaDescrizione(capitoloPrecedente.getDescrizione(), capitoloVariazione.getDescrizione()));
		result.setDescrizioneArticoloPreVariazione(ottieniStringaDescrizione(capitoloPrecedente.getDescrizioneArticolo(), capitoloVariazione.getDescrizioneArticolo()));
		
		List<ClassificatoreGerarchico> classificatoriModificatiDallaVariazione = dati.getClassificatoriGerarchici();
		List<ClassificatoreGerarchico> classificatoriPrecedenti = dati.getClassificatoriGerarchiciPrecedenti();
		result.setStrutturaAmministrativaContabileCapitoloPreVariazione(ottieniStringaSACFromListaClassificatori(classificatoriPrecedenti, classificatoriModificatiDallaVariazione));
	
		VariazioneCodificaCapitolo variazione = dati.getVariazioneCodificaCapitolo();
		result.setNumeroVariazione(variazione.getNumero());
		result.setUid(variazione.getUid());
		
		if(variazione.getAttoAmministrativo()==null){
			result.setProvvedimentoVariazione("");
		}else{
			result.setProvvedimentoVariazione("" + variazione.getAttoAmministrativo().getAnno() + " / " +variazione.getAttoAmministrativo().getNumero());
		}
		result.setDataVariazione(FormatUtils.formatDate(variazione.getData()));
	
		return result;
	
	}



	
	private static String ottieniStringaDescrizione(String descrizionePrecedente, String descrizioneVariazione){
		if(descrizionePrecedente != null && descrizionePrecedente.equals(descrizioneVariazione)){
			return "";
		}
		return descrizionePrecedente;
	}


	private static String ottieniStringaSACFromListaClassificatori(List<ClassificatoreGerarchico> classificatoriPrecedenti, List<ClassificatoreGerarchico> classificatoriVariazione) {
		ClassificatoreGerarchico sacPrecedente = filtraClassificatoriByTipologia(classificatoriPrecedenti, TipologiaClassificatore.CDC);
		if(sacPrecedente==null){
			sacPrecedente = filtraClassificatoriByTipologia(classificatoriPrecedenti, TipologiaClassificatore.CDR);
		}
		ClassificatoreGerarchico sacVariazione = filtraClassificatoriByTipologia(classificatoriVariazione, TipologiaClassificatore.CDC);
		if(sacVariazione==null){
			sacVariazione = filtraClassificatoriByTipologia(classificatoriVariazione, TipologiaClassificatore.CDR);
		}
		if((sacPrecedente!=null && sacVariazione!=null && sacPrecedente.getUid() == sacVariazione.getUid())
				|| (sacPrecedente == null && sacVariazione== null)
				|| sacPrecedente == null ){
			return "";
		} 
		
		return sacPrecedente.getCodice() + "-" + sacPrecedente.getDescrizione();
	}


	private static ClassificatoreGerarchico filtraClassificatoriByTipologia(List<ClassificatoreGerarchico> classificatori, TipologiaClassificatore... tipologiaclassificatoriDaOttenere) {
		if(classificatori == null){
			return null;
		}
	
		for(ClassificatoreGerarchico classificatoreGerarchico : classificatori){
			for(TipologiaClassificatore tc : tipologiaclassificatoriDaOttenere){
				if(tc != null && tc.name().equals(classificatoreGerarchico.getTipoClassificatore().getCodice())){
					return classificatoreGerarchico;
				}
			}
		}
		
		return null;
	}	
	
}
